package com.example.maomakis.data.mappers

import android.annotation.SuppressLint
import android.content.Context
import com.example.maomakis.data.local.entity.*
import com.example.maomakis.domain.model.*

fun User.toModel() = UserModel(
    id = id,
    displayName = name,
    email = email
)

fun UserRegisterModel.toEntity() = User(
    name = name,
    email = email,
    password = password
)


@SuppressLint("DiscouragedApi")
fun Category.toListModel(context: Context): CategoryListModel {
    val iconResId = iconResName?.let {
        val resourceId = context.resources.getIdentifier(it, "drawable", context.packageName)
        if (resourceId == 0) null else resourceId
    }
    return CategoryListModel(id, name, description, iconResId)
}

fun CategoryRegisterModel.toEntity() = Category(id, name, description, iconResName)


@SuppressLint("DiscouragedApi")
fun Product.toListModel(context: Context): ProductListModel {
    val iconResId = iconResName?.let {
        val resourceId = context.resources.getIdentifier(it, "drawable", context.packageName)
        if (resourceId == 0) null else resourceId
    }
    return ProductListModel(
        id = id,
        category = categoryId.toString(),
        favorite = favorite == 1,
        score = score,
        name = name,
        description = description,
        iconResName = iconResId
    )
}

fun ProductRegisterModel.toEntity() = Product(
    id = id,
    categoryId = categoryId,
    favorite = if (favorite) 1 else 0,
    score = score,
    name = name,
    description = description,
    iconResName = iconResName
)
