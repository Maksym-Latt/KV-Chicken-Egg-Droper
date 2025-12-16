package com.chicken.eggdropper.ui.screens.skins

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chicken.eggdropper.R
import com.chicken.eggdropper.model.ChickenSkin
import com.chicken.eggdropper.model.SkinsUiState
import com.chicken.eggdropper.ui.components.CoinsPill
import com.chicken.eggdropper.ui.components.GradientIconButton
import com.chicken.eggdropper.ui.components.OutlineText
import com.chicken.eggdropper.ui.components.SecondaryButton

@Composable
fun SkinsScreen(
    state: SkinsUiState,
    onBack: () -> Unit,
    onSelect: (String) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.bg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                GradientIconButton(
                    iconRes = R.drawable.ic_left_arrow,
                    onClick = onBack
                )

                CoinsPill(
                    coins = state.coins,
                    eggIconRes = R.drawable.item_egg
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(top = 6.dp, bottom = 18.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(state.skins) { skin ->
                    SkinCard(
                        skin = skin,
                        isSelected = skin.id == state.selectedSkin.id,
                        isOwned = state.purchasedSkins.contains(skin.id) || skin.price == 0,
                        coins = state.coins,
                        onSelect = { onSelect(skin.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun SkinCard(
    skin: ChickenSkin,
    isSelected: Boolean,
    isOwned: Boolean,
    coins: Int,
    onSelect: () -> Unit
) {
    val shape = RoundedCornerShape(22.dp)
    val canAfford = isOwned || skin.price == 0 || coins >= skin.price

    val actionState = when {
        isSelected -> ShopActionState.Selected
        isOwned || skin.price == 0 -> ShopActionState.Owned
        canAfford -> ShopActionState.CanBuy
        else -> ShopActionState.NotEnough
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(Color(0x1ad9f3ff))
            .border(2.dp, Color(0xff2b2b2b), shape)
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = skin.idleRes),
                contentDescription = null,
                modifier = Modifier.fillMaxHeight(),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(14.dp))

            Column {
                val nameParts = remember(skin.name) {
                    skin.name.split(" ", limit = 2)
                }
                OutlineText(
                    text = nameParts.getOrNull(0) ?: "",
                    fontSize = 36.sp,
                    brush = SolidColor(Color.White),
                    borderColor = Color(0xff2f2f2f)
                )

                OutlineText(
                    text = nameParts.getOrNull(1) ?: "",
                    fontSize = 36.sp,
                    brush = SolidColor(Color.White),
                    borderColor = Color(0xff2f2f2f),
                    modifier = Modifier.offset(y = (-6).dp)
                )
                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    ShopActionButton(
                        text = when (actionState) {
                            ShopActionState.Selected -> "Selected"
                            ShopActionState.Owned -> "Select"
                            ShopActionState.CanBuy -> skin.price.toString()
                            ShopActionState.NotEnough -> skin.price.toString()
                        },
                        state = actionState,
                        showEgg = actionState == ShopActionState.CanBuy || actionState == ShopActionState.NotEnough,
                        eggIconRes = R.drawable.item_egg,
                        onClick = onSelect
                    )
                }
            }
        }
    }
}

private enum class ShopActionState {
    NotEnough,
    CanBuy,
    Owned,
    Selected
}

@Composable
private fun ShopActionButton(
    text: String,
    state: ShopActionState,
    showEgg: Boolean,
    eggIconRes: Int,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(14.dp)

    val enabled = state == ShopActionState.CanBuy || state == ShopActionState.Owned

    val gradient = when (state) {
        ShopActionState.CanBuy -> Brush.verticalGradient(listOf(Color(0xFFE9B15A), Color(0xFFB17819)))
        ShopActionState.Owned -> Brush.verticalGradient(listOf(Color(0xFFFFD16A), Color(0xFFB17819)))
        ShopActionState.Selected -> Brush.verticalGradient(listOf(Color(0xFFBEBEBE), Color(0xFF8E8E8E)))
        ShopActionState.NotEnough -> Brush.verticalGradient(listOf(Color(0xFF9C9C9C), Color(0xFF6E6E6E)))
    }

    val borderColor = when (state) {
        ShopActionState.NotEnough -> Color(0xFF2B2B2B)
        ShopActionState.CanBuy -> Color(0xFF2B2B2B)
        ShopActionState.Owned -> Color(0xFF2B2B2B)
        ShopActionState.Selected -> Color(0xFF2B2B2B)
    }

    val contentAlpha = when (state) {
        ShopActionState.NotEnough -> 0.65f
        else -> 1f
    }

    Box(
        modifier = Modifier
            .height(56.dp)
            .fillMaxWidth(0.56f)
            .clip(shape)
            .background(gradient, shape)
            .border(2.dp, borderColor, shape)
            .clickable(
                enabled = enabled,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick() }
            .padding(horizontal = 14.dp)
            .alpha(contentAlpha),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlineText(
                text = text,
                fontSize = 18.sp,
                stretchExpand = false,
                borderSize = 7f,
                brush = SolidColor(Color.White),
                borderColor = Color(0xFF2B2B2B)
            )
            if (showEgg) {
                Spacer(modifier = Modifier.width(8.dp))
                Image(
                    painter = painterResource(id = eggIconRes),
                    contentDescription = null,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}
