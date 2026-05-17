import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.mustafa.project001.utils.getDictionarySearchFlow
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalMaterial3Api::class)


@Composable
fun SimilarWordCard(
    word: String,
    navController: NavHostController
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                val encodedWord = URLEncoder.encode(word, StandardCharsets.UTF_8.toString())
                navController.navigate("singleWord/$encodedWord")
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = word,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF2D2D2D)
                )
            )

            Text(
                text = "→",
                color = Color.LightGray,
                fontSize = 20.sp
            )
        }

        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp,
            color = Color.Black.copy(alpha = 0.3f)
        )
    }
}


@Composable
fun DictionarySearchRow(
    text: TextFieldValue,
    navController: NavHostController
) {
    val dictionary = remember {
        listOf(
            "خداوند ج", "محمد ص", "پیغمبر", "قرانکریم", "ابوبکرصدیق", "مسلمان","دین", "روح", "تقوا", "دعا", "ختم قران شریف", "سوگند", "بهشت", "تورات", "فاتحه", "تیمم",
            "مدینه", "سوره", "حاجی", "مقتدی", "قاری", "خیرات","برکت", "فرشته", "جای نماز", "ذکات", "نمازجنازه", "تعمت", "آیت شریف", "جماعت", "سجده", "نماز",
            "رکوع", "تراویح", "تبلیغ", "رمضان", "نامشروع", "شهید","سنت", "نصیب", "گناه", "عیسویت", "نکاح", "کفاره روزه", "حلال", "حرام", "انجیل شریف", "افتار",
            "حق", "أذان", "جهاد", "بنده", "اسلام", "عبادت", "ایمان", "جبرائیل", "عیسوی", "فرض", "کفار", "غنیمت", "آخرت", "وضو", "رکعت", "تسبیح", 
            "عیدقربان", "عشر", "نمازقضا", "ثواب", "دوزخ", "اصحاب", "فدیه روزه", "اجر", "خوش آمدید", "چطورهستی", "اسم", 
            "بی‌ادب", "احترام", "سلام", "محفل", "فرزندی", "بلی", "کارت", "تبریک", "سلام", "صمیمی", "خداحافظ", "تشکر", "شکر", "خانم", "مشکل", "پست", "سالگره", "معلومات", "نوبت",
             "تخلص", "فرق نمی‌کند", "باادب", "ببخشید", "معذرت خواستن", "دوست", "عید", "اجازه", "تیم", "بزرگان", "مجرد", "رسم رواج", "رفتار", "آقا",
              "همسایه", "راز ونیاز", "نخیر", "بادار", "مقام", "نام", "بی اتفاقی", "مهمان", "آواره", "علاقه", "دوستت دارم", "ملاقات", "خوش اخلاق", "بداخلاق",
              "عمر", "به من چی", "آفرین", "برادر", "مرد", "پدر", "طفل", "فامیل", "دوگانگی", "طفل شیرخوار", "همباق", "پسر", "کاکا", "خسر", "کاکا", "خشو", "اطفال", 
              "خواهرزاده", "طفل", "اعضا", "پدرکلان", "خسربره", "خانم", "خواهر", "مادر", "کلان", "مادر", "دخترم", "دختر", "شوهر", "پسرم", "بیوه", "برادرزاده", "خاله", "والدین", 
              "ماما", "داماد", "زن", "نامزدشدن", "نامزد", "اقارب", "عروس", "خیاشنه", "مجرد", 

              "زبان", "گوش", "آبرو", "ابرو","دست", "دهن", "انگشتان دست", "عضله", "بروت", "رویش", "شش", "ناخن", "ساق پا", "کف پا", "قفس سینه", "چهره", "زنخ",
               "اعصاب", "کف دست", "دندان", "قبرغه", "چشم", "آرنج", "گلو", "قلب", "معده", "بنددست", "مغز", "پاشنه","پا", "گرده", "روده", "قد", "بغل", "مشت",
                "شانه", "زانو", "اسکلت", "بینی", "موی", "پیشانی", "مژه", "جمجمه", "سر", "جلد", "لب", "لب", "ورید", "جیگر", "ران", "ستون فقرات", "کمر", "بجلک پا", "بدن", "انگشت", "پشت", 
            
            "زنجیرک", "جیب", "جراب", "چپلک",  "یونیفرم", "ساعت جیبی", "کلاه", "بوت", "پتو", 
            "صابون", "ناخن گیر", "عطر", "خینه", "روغن موی", "شانه موی", "آستین", "کرتی", "لباس سپورتی", "دکمه", "چپن لباس", "عینک",
                "انگشتانه", "زیر پیراهنی", "لاشتک", "لبسیرین", "گردنبند", "گل موی", "انگشتر", "دستبند", "گوشواره", "پیراهن", "کریم", "واسکت", "بیگدی", "جاکت", "یخن قاق",
                 "ساری", "کمربند", "تنبان", "چادری", "دستار", "چپلی", "پیشبند", "شامپو", "بندتنبان", "شال", "طلا", "دستکول", "واسلین", "نیکتایی", "مهره", "دستکش", "موزه", 
                 "بالاپوش", "الماس", "دامن", "پتلولون", "ساعت دستی",  "چادر", "لاجورد", "دستمال بینی", "لباس", 
                 
                 "بطری خورد", "اشتوپ", "داش", "آبگرمی", "تیل", "قالین", "ایرکندیشن", "ویدیو", "بخاری برقی", "ویدیو", 
                 "تلک", "شمع", "زینه", "قفل", "کاغذ تشناب", "بیک", "گلدان", "آینه", "کرایه", "لایتر", "پل ریش", "دیوار", "اتاق", "خانه", "عکس", "بکس جیبی", "تنور", "پیمانه", "کلید",
                  "ساعت دیواری", "فرش", "پرده", "فرش", "پرده", "گلن", "دروازه", "چوکی", "سوزن", "کلکین", "شیشه", "جک",  "چمگک","خاک انداز", "جاروب", "یخچال", "باطله دانی", "نمد", "تیل خاک",
                  "بوجی", "دسترخوان", "دایره","اریکین", "آشپزخانه", "میتر", "اتاق خواب", "پمبه", "مهمانخانه", "سطل", "آفتابه", "روک میز", "چراغ دستی", "سقف", "بام", "تخت خواب", "رادیو", "نل آب",
                "دودکش", "چاه", "گیس", "چپرکت", "گوگرد", "ساکت", "دستشویی", "میز", "جوال دوز", "الماری","بمبه", "کندو", "کارد", "أبکشی", "پودر", "روجایی", "چاقو", "گهواره", "بوتل", "کوزه", "نمکدان",
                 "دوشک", "کاسه", "تشناب", "چلوصاف", "کفگیر", "میوه‌دانی", "غربال", "دیگ", "پطنوس", "قندانی", "لگن", "قیف", "زیرمیزی", "آلبوم", "لحاف", "روی پاک", "دست پاک", "تصویر",
                "سرمیزی", "سیم", "کست", "صندلی", "خریطه", "کوچ وچوکی", "بخاری دیزلی", "اتو", "گروپ", "عصا", "برس بوت", "برس دندان", "چتری", "بالشت", "کوتبند", 
        )
    }

    // 1. Get results from the Flow (results are List<String>)
    val searchResults by produceState<List<String>>(initialValue = emptyList(), text.text) {
        getDictionarySearchFlow(text.text, dictionary).collect { value = it }
    }
//    DictionarySearchExample(navController= navController)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // Show results count
        if (text.text.isNotEmpty()) {
            Text(
                text = "🔍 Found ${searchResults.size} similar word(s)",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        // 2. Display search results
        if (searchResults.isNotEmpty()) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.heightIn(max = 400.dp) // Prevents infinite height issues
            ) {
                items(searchResults) { word -> // 'word' is a String here
                    SimilarWordCard(
                        word = word,
                        navController = navController
                    )
                }
            }
        } else if (text.text.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE))
            ) {
                Text(
                    text = "❌ No similar words found for '${text.text}'",
                    modifier = Modifier.padding(12.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Red
                )
            }
        }
    }
}

// ========== 6. EXTRA: VISUAL SIMILARITY MATRIX ==========
@Composable
fun VisualSimilarityMatrix(input: String) {
    val dictionary = listOf("سلام", "خدا", "حافظ", "عشق")

    // Collect similarity data from the flow
    // Note: This requires getDictionarySearchFlow to return a list of words or objects with scores
    val searchResults by produceState<List<String>>(initialValue = emptyList(), input) {
        getDictionarySearchFlow(input, dictionary).collect { value = it }
    }

    Card(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            searchResults.forEach { word ->
                // Since getDictionarySearchFlow already filtered these,
                // we just display them.
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = word)
                    Text(text = "Match Found", color = Color.Green)
                }
            }
        }
    }
}



//@Composable
//fun DictionarySearchExample(
//    navController: NavHostController
//) {
//    var textFieldValue by remember { mutableStateOf(TextFieldValue("")) }
//
//    Column {
//        // Text Field
//        OutlinedTextField(
//            value = textFieldValue,
//            onValueChange = { textFieldValue = it },
//            label = { Text("Search in dictionary") },
//            placeholder = { Text("Type a word...") },
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp),
//            colors = OutlinedTextFieldDefaults.colors(
//                // Merge all color definitions here
//                focusedTextColor = Color.Black,
//                unfocusedTextColor = Color.Black,
//                cursorColor = Color.Black,
//                // Using your preferred Black borders
//                focusedBorderColor = Color.Black,
//                unfocusedBorderColor = Color.Black.copy(alpha = 0.5f),
//                // Icon and Placeholder colors
//                focusedLeadingIconColor = Color.Black,
//                unfocusedLeadingIconColor = Color.Black,
//                focusedTrailingIconColor = Color.Black,
//                unfocusedTrailingIconColor = Color.Black,
//                focusedPlaceholderColor = Color.Black.copy(alpha = 0.7f),
//                unfocusedPlaceholderColor = Color.Black.copy(alpha = 0.7f)
//            ),
//            singleLine = false,
//            maxLines = 3
//        )
//
//        // Dictionary Search Results (visual)
//        DictionarySearchRow(text = textFieldValue, navController = navController)
//
//        // Visual similarity visualization
//        if (textFieldValue.text.isNotEmpty()) {
//            VisualSimilarityMatrix(textFieldValue.text)
//        }
//    }
//}
