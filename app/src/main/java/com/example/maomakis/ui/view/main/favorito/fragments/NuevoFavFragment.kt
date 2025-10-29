package com.example.maomakis.ui.view.main.favorito.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.maomakis.R
import com.example.maomakis.databinding.FragmentFavNuevoBinding
import com.example.maomakis.databinding.ItemFavoritoVerBinding
import com.example.maomakis.ui.factory.ViewModelFactory
import com.example.maomakis.ui.view.adapter.ProductoAdapter
import com.example.maomakis.ui.view.main.detail.FavoriteProductDetailBottomSheet
import com.example.maomakis.ui.viewmodel.ProductViewModel
import kotlinx.coroutines.launch

class NuevoFavFragment : Fragment() {
    private var _binding: FragmentFavNuevoBinding? = null
    private val binding get() = _binding!!

    private val productViewModel: ProductViewModel by activityViewModels {
        ViewModelFactory(requireActivity().application, requireActivity())
    }

    private lateinit var productAdapter: ProductoAdapter<ItemFavoritoVerBinding>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavNuevoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupProductRecycler()
        loadLast10Products()
    }
    private fun setupProductRecycler() {
        productAdapter = ProductoAdapter(
            bindingInflater = ItemFavoritoVerBinding::inflate,
            binder = { itemBinding, product ->
                itemBinding.idName.text = product.name
                itemBinding.idDescription.text = product.description
                itemBinding.idPrice.text = getString(R.string.currency_format, product.price)
                itemBinding.idRating.text = product.rating.toString()
                product.iconResName?.let { itemBinding.destacadoImg.setImageResource(it) }

                itemBinding.root.setOnClickListener {
                    val sheet = FavoriteProductDetailBottomSheet.newInstance(product.id)
                    sheet.show(parentFragmentManager, "ProductDetail")
                }
            }
        )

        binding.recyclerViewNuevoIntoFavorito.apply {
            adapter = productAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun loadLast10Products() {
        viewLifecycleOwner.lifecycleScope.launch {
            val products = productViewModel.getProductsLast10Static()
            productAdapter.submitList(products)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}