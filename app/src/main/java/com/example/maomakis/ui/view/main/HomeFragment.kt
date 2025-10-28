package com.example.maomakis.ui.view.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.maomakis.R
import com.example.maomakis.databinding.FragmentHomeBinding
import com.example.maomakis.databinding.HomeVerticalItemBinding
import com.example.maomakis.ui.view.adapter.CategoryAdapter
import com.example.maomakis.ui.factory.ViewModelFactory
import com.example.maomakis.ui.view.adapter.ProductoAdapter
import com.example.maomakis.ui.view.main.detail.ProductDetailBottomSheet
import com.example.maomakis.ui.viewmodel.CategoryViewModel
import com.example.maomakis.ui.viewmodel.CarritoViewModel
import com.example.maomakis.ui.viewmodel.ProductViewModel
import com.example.maomakis.ui.viewmodel.UserViewModel
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    // ViewModels
    private val productViewModel: ProductViewModel by activityViewModels {
        ViewModelFactory(requireActivity().application, requireActivity())
    }
    private val categoryViewModel: CategoryViewModel by activityViewModels {
        ViewModelFactory(requireActivity().application, requireActivity())
    }
    private val userViewModel: UserViewModel by activityViewModels {
        ViewModelFactory(requireActivity().application, requireActivity())
    }
    private lateinit var carritoViewModel: CarritoViewModel

    // Adapters
    private lateinit var productAdapter: ProductoAdapter<HomeVerticalItemBinding>
    private lateinit var categoryAdapter: CategoryAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = ViewModelFactory(requireActivity().application, this)
        carritoViewModel = ViewModelProvider(this, factory)[CarritoViewModel::class.java]

        setupProductRecycler()
        setupCategoryRecycler()
        observeCategories()
        observeUserGreeting()
    }

    private fun setupProductRecycler() {
        productAdapter = ProductoAdapter(
            // 1. Inflador del ViewBinding para cada item
            bindingInflater = HomeVerticalItemBinding::inflate,
            // 2. Lógica para enlazar datos y listeners
            binder = { itemBinding, product ->
                // Enlazar datos del producto
                itemBinding.name.text = product.name
                itemBinding.price.text = getString(R.string.currency_format, product.price)
                itemBinding.rating.text = product.score.toString()
                itemBinding.timing.text = product.score.toString()
                product.iconResName?.let { itemBinding.verImg.setImageResource(it) }

                // Listener para clic en toda la tarjeta -> abre detalles
                itemBinding.root.setOnClickListener {
                    val detailFragment = ProductDetailBottomSheet.newInstance(
                        product.name,
                        getString(R.string.currency_format, product.price),
                        product.iconResName ?: R.drawable.ic_launcher_foreground,
                        product.id,
                        product.favorite
                    )
                    detailFragment.show(childFragmentManager, detailFragment.tag)
                }

                // Listener para el botón de añadir al carrito
                // IMPORTANTE: Asegúrate de que tu XML tiene un botón con id `add_to_cart_image`
//                itemBinding.addToCartImage.setOnClickListener {
//                    val user = userViewModel.loggedInUser.value
//                    if (user != null) {
//                        carritoViewModel.addProduct(user.id, product.id)
//                        Toast.makeText(requireContext(), "${product.name} añadido al carrito", Toast.LENGTH_SHORT).show()
//                    } else {
//                        Toast.makeText(requireContext(), "Inicia sesión para añadir productos", Toast.LENGTH_SHORT).show()
//                    }
//                }
            }
        )

        binding.homeVerRec.apply {
            adapter = productAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupCategoryRecycler() {
        categoryAdapter = CategoryAdapter(onCategoryClicked = { category ->
            // Al seleccionar categoría, cargar productos filtrados
            viewLifecycleOwner.lifecycleScope.launch {
                productViewModel.getProductsByCategory(category.id).collect { products ->
                    productAdapter.submitList(products)
                }
            }
        })

        binding.homeHorRec.apply {
            adapter = categoryAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun observeCategories() {
        viewLifecycleOwner.lifecycleScope.launch {
            categoryViewModel.categories.collect { categories ->
                categoryAdapter.submitList(categories)

                // Si hay categorías, precargar productos de la primera
                if (categories.isNotEmpty()) {
                    viewLifecycleOwner.lifecycleScope.launch {
                        productViewModel.getProductsByCategory(categories.first().id).collect { products ->
                            productAdapter.submitList(products)
                        }
                    }
                } else {
                    // Si no hay categorías, puedes mostrar todos los productos
                    productAdapter.submitList(emptyList())
                }
            }
        }
    }

    private fun observeUserGreeting() {
        viewLifecycleOwner.lifecycleScope.launch {
            userViewModel.loggedInUser.collect { user ->
                val greeting = if (user != null && user.displayName.isNotBlank()) {
                    "Hola ${user.displayName}"
                } else {
                    getString(R.string.hola)
                }
                // textView7 es el título grande en Home
                binding.textView7.text = greeting
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
