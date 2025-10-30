package com.example.maomakis.ui.view.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import com.example.maomakis.R
import com.example.maomakis.databinding.FragmentCheckoutBinding
import com.example.maomakis.ui.factory.ViewModelFactory
import com.example.maomakis.ui.viewmodel.CarritoViewModel
import com.example.maomakis.ui.viewmodel.UserViewModel
import androidx.navigation.fragment.findNavController
import com.example.maomakis.domain.model.CarritoModel
import com.example.maomakis.ui.viewmodel.OrderViewModel
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import androidx.navigation.navOptions

class CheckoutFragment : Fragment() {

    private var _binding: FragmentCheckoutBinding? = null
    private val binding get() = _binding!!

    private val userViewModel: UserViewModel by activityViewModels {
        ViewModelFactory(requireActivity().application, requireActivity())
    }

    private fun selectPayment(radioId: Int, label: String) {
        binding.paymentGroup.check(radioId)
        lastPaymentText = label
        updatePaymentCardsUI(radioId)
        updateTotalsAndDetails()
    }

    private fun updatePaymentCardsUI(selectedId: Int) {
        val selectedStroke = resources.getDimensionPixelSize(R.dimen.selected_card_stroke)
        val normalStroke = resources.getDimensionPixelSize(R.dimen.normal_card_stroke)
        binding.cardPaymentCash.strokeWidth = if (selectedId == R.id.payment_cash) selectedStroke else normalStroke
        binding.cardPaymentVisa.strokeWidth = if (selectedId == R.id.payment_card_visa) selectedStroke else normalStroke
        binding.cardPaymentMaster.strokeWidth = if (selectedId == R.id.payment_card_master) selectedStroke else normalStroke
        binding.cardPaymentYape.strokeWidth = if (selectedId == R.id.payment_yape) selectedStroke else normalStroke
    }
    private val carritoViewModel: CarritoViewModel by activityViewModels {
        ViewModelFactory(requireActivity().application, requireActivity())
    }
    private val orderViewModel: OrderViewModel by activityViewModels {
        ViewModelFactory(requireActivity().application, requireActivity())
    }

    private var cartSubtotal: Double = 0.0
    private var deliveryFee: Double = 0.0
    private var lastPaymentText: String = ""
    private var lastDeliveryText: String = ""
    private var lastCartItems: List<CarritoModel> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCheckoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setFragmentResultListener("map_pick_result") { _, bundle ->
            val address = bundle.getString("address").orEmpty()
            if (address.isNotBlank()) {
                binding.inputAddress.setText(address)
            }
        }

        binding.selectOnMapButton.setOnClickListener {
            findNavController().navigate(R.id.nav_map_picker)
        }

        // Selección por tarjetas estilo item_favorito_ver
        binding.cardPaymentCash.setOnClickListener {
            selectPayment(R.id.payment_cash, binding.labelPaymentCash.text.toString())
        }
        binding.cardPaymentVisa.setOnClickListener {
            selectPayment(R.id.payment_card_visa, binding.labelPaymentVisa.text.toString())
        }
        binding.cardPaymentMaster.setOnClickListener {
            selectPayment(R.id.payment_card_master, binding.labelPaymentMaster.text.toString())
        }
        binding.cardPaymentYape.setOnClickListener {
            selectPayment(R.id.payment_yape, binding.labelPaymentYape.text.toString())
        }

        lifecycleScope.launch {
            val user = userViewModel.loggedInUser.filterNotNull().first()

            viewLifecycleOwner.lifecycleScope.launch {
                carritoViewModel.getCartItems(user.id).collect { cartItems ->
                    lastCartItems = cartItems
                    cartSubtotal = cartItems.sumOf { it.subTotal }
                    updateTotalsAndDetails(user.displayName, user.email)
                }
            }
        }

        binding.deliveryGroup.setOnCheckedChangeListener { _, _ ->
            deliveryFee = when (binding.deliveryGroup.checkedRadioButtonId) {
                R.id.delivery_priority -> 12.0
                R.id.delivery_basic -> 8.0
                R.id.delivery_economic -> 5.0
                else -> 0.0
            }
            lastDeliveryText = when (binding.deliveryGroup.checkedRadioButtonId) {
                R.id.delivery_priority -> "Prioritaria (S/ 12.00)"
                R.id.delivery_basic -> "Básica (S/ 8.00)"
                R.id.delivery_economic -> "Económica (S/ 5.00)"
                else -> ""
            }
            updateTotalsAndDetails()
        }

        binding.paymentGroup.setOnCheckedChangeListener { _, checkedId ->
            lastPaymentText = view?.findViewById<RadioButton>(checkedId)?.text?.toString().orEmpty()
            updatePaymentCardsUI(checkedId)
            updateTotalsAndDetails()
        }

        binding.placeOrderButton.setOnClickListener {
            val address = binding.inputAddress.text?.toString()?.trim().orEmpty()
            if (address.isEmpty()) {
                Toast.makeText(requireContext(), "Ingresa una dirección", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val paymentId = binding.paymentGroup.checkedRadioButtonId
            val deliveryId = binding.deliveryGroup.checkedRadioButtonId
            if (paymentId == View.NO_ID || deliveryId == View.NO_ID) {
                Toast.makeText(requireContext(), "Selecciona método de pago y envío", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val payment = view?.findViewById<RadioButton>(paymentId)?.text?.toString().orEmpty()
            val delivery = view?.findViewById<RadioButton>(deliveryId)?.text?.toString().orEmpty()

            lifecycleScope.launch {
                val user = userViewModel.loggedInUser.filterNotNull().first()
                val subtotal = cartSubtotal
                val total = cartSubtotal + deliveryFee
                val items = lastCartItems
                orderViewModel.placeOrderAndClearCart(
                    userId = user.id,
                    userName = user.displayName,
                    userEmail = user.email,
                    address = address,
                    deliveryType = delivery,
                    deliveryFee = deliveryFee,
                    paymentMethod = payment,
                    subtotal = subtotal,
                    total = total,
                    items = items,
                    onDone = { orderId ->
                        Toast.makeText(requireContext(), "Pedido realizado correctamente", Toast.LENGTH_LONG).show()
                        val args = Bundle().apply { putLong("orderId", orderId) }
                        val options = navOptions {
                            popUpTo(R.id.nav_checkout) { inclusive = true }
                        }
                        findNavController().navigate(R.id.nav_order_confirmation, args, options)
                    },
                    onError = {
                        Toast.makeText(requireContext(), "Error al guardar pedido", Toast.LENGTH_LONG).show()
                    }
                )
            }
        }
    }

    private fun updateTotalsAndDetails(userName: String? = null, userEmail: String? = null) {
        val total = cartSubtotal + deliveryFee
        binding.totalCheckout.text = getString(R.string.currency_format, total)

        val nameText = userName ?: userViewModel.loggedInUser.value?.displayName.orEmpty()
        val emailText = userEmail ?: userViewModel.loggedInUser.value?.email.orEmpty()

        val address = binding.inputAddress.text?.toString()?.trim().orEmpty()

        val deliveryText = lastDeliveryText.ifBlank {
            when (binding.deliveryGroup.checkedRadioButtonId) {
                R.id.delivery_priority -> "Prioritaria (S/ 12.00)"
                R.id.delivery_basic -> "Básica (S/ 8.00)"
                R.id.delivery_economic -> "Económica (S/ 5.00)"
                else -> "No seleccionado"
            }
        }

        val paymentText = lastPaymentText.ifBlank {
            val checked = binding.paymentGroup.checkedRadioButtonId
            view?.findViewById<RadioButton>(checked)?.text?.toString().orEmpty().ifBlank { "No seleccionado" }
        }

        // Construir detalles con información de usuario, envío, pago y totales
        val cartLines = if (lastCartItems.isEmpty()) emptyList() else lastCartItems.map { item ->
            "${item.cant} x ${item.name} - " + getString(R.string.currency_format, item.subTotal)
        }
        val header = buildString {
            appendLine("Cliente: $nameText")
            appendLine("Correo: $emailText")
            appendLine("Dirección: ${if (address.isBlank()) "(sin dirección)" else address}")
            appendLine("Envío: $deliveryText")
            appendLine("Pago: $paymentText")
            appendLine("Subtotal: ${getString(R.string.currency_format, cartSubtotal)}")
            appendLine("Envío: ${getString(R.string.currency_format, deliveryFee)}")
            appendLine("Total: ${getString(R.string.currency_format, total)}")
            appendLine("")
            appendLine("Items:")
        }

        // Reconstruir items desde el subtotal actual: volvemos a listar desde ViewModel si es necesario
        // Para simplicidad, mantenemos las líneas ya generadas si existen, sino no mostramos items aquí.
        val detailsText = header + cartLines.joinToString(separator = "\n")

        binding.orderDetails.text = detailsText
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
