package com.example.maomakis.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.maomakis.databinding.FragmentHomeBinding
import com.example.maomakis.ui.adapter.ProductAdapter
import com.example.maomakis.ui.adapter.CategoryAdapter
import com.example.maomakis.ui.factory.ViewModelFactory
import com.example.maomakis.ui.viewmodel.CarritoViewModel
import com.example.maomakis.ui.viewmodel.ProductViewModel
import com.example.maomakis.ui.viewmodel.UserViewModel
import com.example.maomakis.ui.viewmodel.CategoryViewModel
import kotlinx.coroutines.launch
import com.example.maomakis.R
import com.example.maomakis.ui.detail.ProductDetailBottomSheet

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    // ViewModel para obtener la lista de productos.
    private val productViewModel: ProductViewModel by activityViewModels {
        ViewModelFactory(requireActivity().application, requireActivity())
    }

    // ViewModel para categorías
    private val categoryViewModel: CategoryViewModel by activityViewModels {
        ViewModelFactory(requireActivity().application, requireActivity())
    }

    // ViewModel compartido para saber quién es el usuario.
    private val userViewModel: UserViewModel by activityViewModels {
        ViewModelFactory(requireActivity().application, requireActivity())
    }

    // ViewModel para el carrito
    private lateinit var carritoViewModel: CarritoViewModel
    private lateinit var productAdapter: ProductAdapter
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
        productAdapter = ProductAdapter(
            onAddToCartClicked = { product ->
                val user = userViewModel.loggedInUser.value
                if (user != null) {
                    carritoViewModel.addProduct(user.id, product.id)
                    Toast.makeText(requireContext(), "${product.name} añadido al carrito", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Inicia sesión para añadir productos", Toast.LENGTH_SHORT).show()
                }
            },
            onItemClicked = { product ->
                val detailFragment = ProductDetailBottomSheet.newInstance(
                    product.name,
                    getString(R.string.currency_format, product.price),
                    product.iconResName ?: R.drawable.ic_launcher_foreground,
                    product.id
                )
                detailFragment.show(childFragmentManager, detailFragment.tag)
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
                    getString(com.example.maomakis.R.string.hola)
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