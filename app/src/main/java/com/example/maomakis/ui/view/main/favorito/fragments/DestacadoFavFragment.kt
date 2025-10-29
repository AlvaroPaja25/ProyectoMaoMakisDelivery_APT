package com.example.maomakis.ui.view.main.favorito.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.maomakis.R
import com.example.maomakis.databinding.FragmentFavDestacadoBinding
import com.example.maomakis.databinding.ItemFavoritoBinding
import com.example.maomakis.ui.factory.ViewModelFactory
import com.example.maomakis.ui.view.adapter.ProductoAdapter
import com.example.maomakis.ui.viewmodel.CarritoViewModel
import com.example.maomakis.ui.viewmodel.ProductViewModel
import com.example.maomakis.ui.viewmodel.UserViewModel
import kotlinx.coroutines.launch

class DestacadoFavFragment : Fragment() {

    private var _binding: FragmentFavDestacadoBinding? = null
    private val binding get() = _binding!!

    private val productViewModel: ProductViewModel by activityViewModels {
        ViewModelFactory(requireActivity().application, requireActivity())
    }
    private val userViewModel: UserViewModel by activityViewModels {
        ViewModelFactory(requireActivity().application, requireActivity())
    }
    private lateinit var carritoViewModel: CarritoViewModel
    private lateinit var productAdapter: ProductoAdapter<ItemFavoritoBinding>


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavDestacadoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = ViewModelFactory(requireActivity().application, this)
        carritoViewModel = ViewModelProvider(this, factory)[CarritoViewModel::class.java]

        setupProductRecycler()
        observeFavorites()
    }

    private fun setupProductRecycler() {
        productAdapter = ProductoAdapter(
            bindingInflater = ItemFavoritoBinding::inflate,
            binder = { itemBinding, product ->
                // Enlazar datos del producto
                itemBinding.idName.text = product.name
                itemBinding.idDescription.text = product.description
                itemBinding.idPrice.text = getString(R.string.currency_format, product.price)
                itemBinding.idRating.text = product.rating.toString()
                product.iconResName?.let { itemBinding.img.setImageResource(it) }
                itemBinding.favoriteButton.setImageResource(
                    if (product.favorite) R.drawable.ic_favorite_filled else R.drawable.ic_favorite_border
                )
//                 Click para toggle favorito: delegamos al ViewModel
                itemBinding.favoriteButton.setOnClickListener {
                    // Opcional: validar sesión primero
                    val user = userViewModel.loggedInUser.value
                    if (user != null) {
                        productViewModel.toggleFavorite(product.id, product.favorite)
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Inicia sesión para añadir a favoritos",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        )

        binding.recyclerViewDestacadoVer.apply {
            adapter = productAdapter
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL,false)
        }
    }

    private fun observeFavorites() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                productViewModel.products.collect { products ->
                    productAdapter.submitList(products)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
