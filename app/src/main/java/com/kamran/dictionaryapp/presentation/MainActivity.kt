package com.kamran.dictionaryapp.presentation

import android.os.Bundle
import android.widget.Space
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kamran.dictionaryapp.domain.models.Meaning
import com.kamran.dictionaryapp.domain.models.WordItem
import com.kamran.dictionaryapp.ui.theme.DictionaryAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DictionaryAppTheme {
                // A surface container using the 'background' color from the theme
                /*Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                }*/

                val mainViewModel = hiltViewModel<MainViewModel>()
                val mainState by mainViewModel.mainState.collectAsState()


                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 30.dp),
                            value = mainState.searchWord,
                            onValueChange = {
                                mainViewModel.onEvent(
                                    MainUiEvents.OnSearchWordChange(it)
                                )
                            },
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Rounded.Search,
                                    contentDescription = "Search a word",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier
                                        .size(30.dp)
                                        .clickable {
                                            mainViewModel.onEvent(
                                                MainUiEvents.OnSearchClick
                                            )
                                        }

                                )
                            },
                            label = {
                                Text(
                                    text = "Search a word",
                                    fontSize = 15.sp,
                                    modifier = Modifier.alpha(0.7f)
                                )
                            },
                            textStyle = TextStyle(
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = 19.5.sp
                            )
                        )
                    }
                ) { paddingValues ->

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = paddingValues.calculateTopPadding())
                    ) {
                        MainScreen(mainState)
                    }
                }
            }
        }
    }

    @Composable
    private fun MainScreen(mainState: MainState) {

        Box(modifier = Modifier.fillMaxSize()) {

            if (mainState.wordItem?.word?.contains("Can't get result") == true) {
                Text(
                    text = "Can't get result",
                    fontSize = 20.sp,
                    modifier = Modifier.padding(horizontal = 20.dp),
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary.copy(0.8f)
                )
                return
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(MaterialTheme.colorScheme.background)
                    .padding(horizontal = 30.dp)
            ) {
                mainState.wordItem?.let { wordItem ->

                    Text(
                        text = wordItem.word,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = wordItem.phonetic,
                        fontSize = 17.sp,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                }
            }

            Box(

                modifier = Modifier
                    .padding(top = 110.dp)
                    .fillMaxSize()
                    .clip(
                        RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
                    )
                    .background(MaterialTheme.colorScheme.secondaryContainer.copy(0.7f))
            ) {
                if (mainState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(80.dp)
                            .align(Alignment.Center),
                        color = MaterialTheme.colorScheme.primary

                    )

                } else {
                    mainState.wordItem?.let { wordItem ->
                        WordResult(wordItem)
                    }
                }
            }


        }
    }


    @Composable
    fun WordResult(wordItem: WordItem) {
        LazyColumn(
            contentPadding = PaddingValues(vertical = 32.dp)
        ) {
            items(wordItem.meanings.size) { index ->
                Meaning(
                    meaning = wordItem.meanings[index],
                    index = index
                )
                Spacer(modifier = Modifier.height(32.dp))

            }
        }

    }

    @Composable
    fun Meaning(
        meaning: Meaning,
        index: Int
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "${index + 1}. ${meaning.partOfSpeech}",
                fontSize = 17.sp,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        brush = Brush.horizontalGradient(
                            listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.primary.copy(0.4f),
                                Color.Transparent
                            )
                        )
                    )
                    .padding(
                        top = 2.dp, bottom = 2.dp,
                        start = 12.dp, end = 12.dp
                    )
            )

            if (meaning.definitions.definition.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    Text(
                        text = "Definition:",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 17.sp,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = meaning.definitions.definition,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                }
            }
            if (meaning.definitions.example.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    Text(
                        text = "Example:  ",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 17.sp,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = meaning.definitions.example,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                }
            }

        }
    }

}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DictionaryAppTheme {

    }
}