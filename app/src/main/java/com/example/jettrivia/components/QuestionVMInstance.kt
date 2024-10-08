package com.example.jettrivia.components


import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jettrivia.model.QuestionItems
import com.example.jettrivia.util.AppColors
import com.example.jettrivia.viewmodel.QuestionViewModel

@Composable
fun QuestionFun(viewModel: QuestionViewModel) {
    val questionData = viewModel.dataFromRepo.value.data?.toMutableList()
    val questionViewModel = viewModel.dataFromRepo.value
    val questionCount = remember{mutableStateOf(0)}
    val totalCountOfQuestion = questionData?.size

    if (questionViewModel.loading == true) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        val question = if (questionData!!.get(questionCount.value) != null) {
            questionData!!.get(questionCount.value)
        } else {
            null
        }
        if (questionData != null) {
            QuestionScreen(question!!, questionCount, totalCountOfQuestion) {
                questionCount.value = questionCount.value + 1
            }
        }
    }
}

//@Preview()
@Composable
fun QuestionScreen(
    questionItems: QuestionItems,
    questionCount: MutableState<Int>,
    totalCountOfQuestion: Int?,
    onNextClicked: (Int) -> Unit
) {
    val pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
    val qCount = questionCount
    Surface(
        modifier = Modifier.fillMaxSize()
            .padding(4.dp),
        color = AppColors.mDarkPurple
    ) {
        Column() {
            if (questionCount.value > 3) ShowProgressBar(questionCount.value)
            QuestionTracker(qCount, totalCountOfQuestion)
            DottedLine(pathEffect)
            QuestionBody(questionItems, qCount, onNextClicked)
        }
    }
}


/*
 * Creating question tracker to show how many question is solved
 */
@Composable
fun QuestionTracker(qCount: MutableState<Int>, totalCountOfQuestion: Int?) {
    Text(
        text = buildAnnotatedString{
            withStyle(
                style = ParagraphStyle(
                    textIndent = TextIndent.None
                )
            ) {
                withStyle(
                    style = SpanStyle(
                        color = AppColors.mLightGray,
                        fontWeight = FontWeight.Bold,
                        fontSize = 32.sp
                    )
                ) {
                    append("Question ${qCount.value+1}/")
                }

                withStyle(
                    style = SpanStyle(
                        color = AppColors.mLightGray,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp
                    )
                ) {
                    append("${totalCountOfQuestion}")
                }
            }
        },
        modifier = Modifier.padding(20.dp)
    )
}

@Composable
fun DottedLine(pathEffect: PathEffect) {
    Canvas(
        modifier = Modifier.fillMaxWidth()
            .height(1.dp)
            .padding(start = 10.dp, end = 10.dp)
    ) {
        drawLine(
            color = AppColors.mLightGray,
            start = Offset(0f, 0f),
            end = Offset(size.width, 0f),
            pathEffect = pathEffect
        )
    }
}

/*
 * Displaying Questions
 */
@Composable
fun QuestionBody(questions: QuestionItems?,
                 questionIndex: MutableState<Int>,
                 //questionViewModel: QuestionViewModel,
                 onNextClicked: (Int) -> Unit = {}) {
    val choices = remember(questions) {
        questions!!.choices.toMutableList()
    }

    val answerState = remember(questions) {
        mutableStateOf<Int?>(null)
    }

    val correctAnswerState = remember(questions) {
        mutableStateOf<Boolean?>(null)
    }

    val updataAnswer: (Int)-> Unit = remember(questions){
        {
            answerState.value = it
            correctAnswerState.value = choices[it] == questions!!.answer
        }
    }
    Column(
        modifier = Modifier.padding(10.dp)
    ) {
        Text(
            text = questions!!.question,
            modifier = Modifier.padding(6.dp)
                .align(alignment = Alignment.Start)
                .fillMaxHeight(0.3f),
            fontSize = 18.sp,
            color = AppColors.mOffWhite,
            fontWeight = FontWeight.Bold,
            lineHeight = 22.sp
        )

        choices.forEachIndexed { index, answer ->
            Row (
                modifier = Modifier.padding(3.dp)
                    .fillMaxWidth()
                    .height(45.dp)
                    .border(
                        width = 4.dp, brush = Brush.linearGradient(
                            colors = listOf(
                                Color.DarkGray,
                                AppColors.mLightPurple
                            )
                        ),
                        shape = RoundedCornerShape(15.dp)
                    )
                    .clip(RoundedCornerShape(
                        topStartPercent = 50, topEndPercent = 50,
                        bottomStartPercent = 50, bottomEndPercent = 50
                    ))
                    .background(Color.Transparent),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (answerState.value == index),
                    onClick = {
                        updataAnswer(index)
                    },
                    modifier = Modifier.padding(16.dp),
                    colors = RadioButtonDefaults
                        .colors(
                            selectedColor = if (correctAnswerState.value == true && index == answerState.value) {
                                Color.Green.copy(alpha = 0.2f)
                            } else {
                                Color.Red.copy(alpha = 0.2f)
                            }
                        )
                )

                val rightWrongTextColor = buildAnnotatedString{
                    withStyle(
                        style = SpanStyle(fontWeight = FontWeight.Light,
                            color = if(correctAnswerState.value == true && answerState.value == index) {
                                Color.Green
                            } else if(correctAnswerState.value == false && answerState.value == index) {
                                Color.Red
                            } else {
                                Color.White
                            },
                            fontSize = 18.sp
                        ) ){
                            append(answer)
                        }
                }

                Text(
                    text = rightWrongTextColor
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))
        ElevatedButton(
            onClick = {
                    onNextClicked(questionIndex.value)
            },
            modifier = Modifier.padding(3.dp)
                .align(alignment = Alignment.CenterHorizontally),
            shape = RoundedCornerShape(34.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = AppColors.mLightBlue
            )
        ) {
            Text(
                text = "Next",
                modifier = Modifier.padding(4.dp),
                color = AppColors.mOffWhite,
                fontSize = 17.sp
            )
        }
    }
}

/*
 * Showing progress bar
 */
@Preview
@Composable
fun ShowProgressBar(score: Int = 12) {
    val gradient = Brush.linearGradient(listOf(
        Color(0xfff95075),
        Color(0xffBE6BE5)
    ))

    val progressRatio = remember(score) {
        mutableStateOf(score * 0.005f)
    }.value

    Row(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
            .height(45.dp)
            .border(
                width = 4.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        AppColors.mLightPurple,
                        AppColors.mDarkPurple
                    )
                ),
                shape = RoundedCornerShape(34.dp)
            )
            .clip(RoundedCornerShape(topStartPercent = 50, bottomStartPercent = 50,
                topEndPercent = 50, bottomEndPercent = 50))
            .background(Color.Transparent),
        verticalAlignment = Alignment.CenterVertically

    ) {
        Button(
            contentPadding = PaddingValues(1.dp),
            onClick = {},
            modifier = Modifier.fillMaxWidth(progressRatio)
                .background(brush = gradient),
            enabled = false,
            elevation = null,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent
            )
        ) {
            Text(
                text = "${score * 120}",
                modifier = Modifier.clip(shape = RoundedCornerShape(10.dp))
                    .fillMaxHeight(0.87f)
                    .fillMaxWidth()
                    .padding(6.dp),
                color = AppColors.mOffWhite,
                textAlign = TextAlign.Center
            )

        }
    }
}