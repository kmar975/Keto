package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        Scaffold(
          modifier = Modifier.fillMaxSize(),
          bottomBar = { BottomNavigationBar() }
        ) { innerPadding ->
          KetoBrainHandbookScreen(modifier = Modifier.padding(innerPadding))
        }
      }
    }
  }
}

enum class NavigationTab {
  BIOCHEMISTRY,
  METHODOLOGY
}

enum class SimulatorMode {
  GLUCOSE,
  KETONES
}

data class DetailSection(
  val id: String,
  val title: String,
  val subtitle: String,
  val bulletPoints: List<String>,
  val shockingTakeaway: String
)

@Composable
fun KetoBrainHandbookScreen(modifier: Modifier = Modifier) {
  var activeTab by remember { mutableStateOf(NavigationTab.BIOCHEMISTRY) }
  var simMode by remember { mutableStateOf(SimulatorMode.GLUCOSE) }
  var expandedSectionId by remember { mutableStateOf<String?>("bio_1") }

  val biochemistrySections = remember {
    listOf(
      DetailSection(
        id = "bio_1",
        title = "1. Żarłoczny Mózg i Energetyczny Kryzys",
        subtitle = "Pompa sodowo-potasowa i zapotrzebowanie",
        bulletPoints = listOf(
          "Mózg stanowi tylko 2% masy ciała dziecka lub dorosłego, ale zużywa aż 20% całej energii organizmu.",
          "Każdy pojedynczy aktywny neuron potrzebuje około 4 miliardów cząsteczek ATP na sekundę, głównie dla pomp sodowo-potasowych (przywracających polaryzację napięcia).",
          "Większość zaburzeń psychicznych (mania, lęk, depresja, schizofrenia) ma wspólny biologiczny mianownik: chroniczny deficyt energii w neuronach.",
          "W zespole metabolicznym i insulinooporności mózg traci zdolność spalania cukru, mimo wysokiego poziomu glukozy we krwi. Mózg dosłownie głoduje w morzu obfitości!"
        ),
        shockingTakeaway = "SZOKUJĄCY FAKT: Mózg ma tak ogromne stałe zapotrzebowanie, że najmniejsze spadki napięcia energetycznego skutkują natychmiastowym chaosem w neuroprzekaźnikach – stąd nagłe wahania nastroju i stany lękowe."
      ),
      DetailSection(
        id = "bio_2",
        title = "2. Neurony, Glej i Izolacja Kabli",
        subtitle = "Anatomia komórkowa według dr Ede",
        bulletPoints = listOf(
          "Mózg składa się z ok. 85 miliardów neuronów i co najmniej tylu samo ochronnych komórek glejowych.",
          "Oligodendrocyty owijają aksony neuronów osłonkami mielinowymi (aż do 80 warstw tłuszczu i cholesterolu), zapewniając przepływ prądu z prędkością do 320 km/h.",
          "Astrocyty działają jak systemy logistyczne – bezpośrednio karmią neurony, wychwytują odpady i kształtują połączenia (synapsy).",
          "Mikroglej to armia odpornościowa mózgu. Pod wpływem reaktywnych form tlenu i wysokiej glukozy ulega aktywacji, uwalniając zapalne cytokiny uszkadzające komórki."
        ),
        shockingTakeaway = "SZOKUJĄCY FAKT: Jeśli z powodu diety wysokowęglowodanowej i stanów zapalnych uszkodzeniu ulega lipidowo-cholesterolowa mielina, sygnały elektryczne ulegają rozproszeniu i wyciszeniu, co klinicznie objawia się mgłą mózgową i apatią."
      ),
      DetailSection(
        id = "bio_3",
        title = "3. Silnik G (Glikoliza) vs Silnik M (Mitochondria)",
        subtitle = "Biochemiczne szlaki pozyskiwania ATP",
        bulletPoints = listOf(
          "Silnik G (Glikoliza) zachodzi w płynie komórkowym (cytoplazmie). To szybki, bez tlenu, ale mało sprawny proces: krojenie glukozy na pirogronian. Daje tylko odrobinę energii (szybki, gasnący rozbłysk).",
          "Silnik M (Mitochondrialny) – praca tlenowa w osławionych mitochondriach. Elektrony są przenoszone przez łańcuch 5 potężnych kompleksów białkowych (ETC).",
          "Silnik M generuje około 15 razy więcej czystej, długoterminowej energii (ATP) z tych samych fragmentów paliwowych.",
          "Spalanie paliwa w Silniku M wymaga całej gamy kofaktorów: witamin z grupy B (B1, B2, B3, B5, B7) oraz magnezu i aktywnego żelaza."
        ),
        shockingTakeaway = "SZOKUJĄCY FAKT: Opieranie metabolizmu wyłącznie na Silniku G (z powodu niedoborów lub blokad mitochondrialnych) to biologiczny odpowiednik próby ogrzania domu papierem gazetowym zamiast suchym drewnem dębowym."
      ),
      DetailSection(
        id = "bio_4",
        title = "4. Przekleństwo Glukozy i Blokada Insuliny",
        subtitle = "Mechanizm oporności receptorów mózgowych",
        bulletPoints = listOf(
          "Neurony nie potrafią spalić glukozy bez insuliny. Receptory insuliny na neuronach i na barierze krew-mózg wpuszczają informację metaboliczną do środka.",
          "Z powodu diety bogatej w węglowodany (chleb, makaron, soki, cukier), receptory insuliny ulegają 'ogłuszeniu' i niszczeniu (down-regulation).",
          "Transport glukozy do mózgu drastycznie spada. Powstaje mózgowy hipometabolizm glukozy (cerebral glucose hypometabolism).",
          "Wolne rodniki utworzone z nadmiaru glukozy wywołują niszczący stany zapalne proces glikacji (powstają cząsteczki AGEs doprowadzające do atrofii hipokampa i Alzheimera - cukrzycy typu 3)."
        ),
        shockingTakeaway = "SZOKUJĄCY FAKT: Pacjent z insulinoopornością mózgu może mieć optymalny lub nawet wysoki cukier we krwi, ale jego neurony dosłownie głodują z powodu blokady transportu. Mózg woła o energię wzmagając stany lekowe i depresyjne."
      ),
      DetailSection(
        id = "bio_5",
        title = "5. Ketony jako Rewolucyjny Bypass",
        subtitle = "3 reakcje zamiast 13 i ominięcie Kompleksu I",
        bulletPoints = listOf(
          "Podczas braku węglowodanów wątroba produkuje z kwasów tłuszczowych beta-hydroksymaślan (BHB) – stabilne ciało ketonowe.",
          "Przekształcenie glukozy w Acetyl-CoA wymaga 13 skomplikowanych enzymatycznych reakcji. Przekształcenie ketonu (BHB) w to samo paliwo wymaga tylko 3 prostych kroków!",
          "Wiele zaburzeń psychicznych (np. afektywne dwubiegunowe, schizofrenia) wykazuje uszkodzenie Kompleksu I w mitochondriach, co blokuje elektrony z glukozy.",
          "Ketony (BHB) dostarczają elektrony przez FAD, wchodząc przez Kompleks II i omijając uszkodzony Kompleks I!",
          "Dodatkowo ketony nie potrzebują insuliny do wniknięcia i spalenia w neuronach, z łatwością lecząc chroniczny głód mózgu."
        ),
        shockingTakeaway = "SZOKUJĄCY FAKT: Ketony działają jak doskonały biologiczny instalator, bez problemu omijając uszkodzone i uregulowane w dół insulinowe tory w chorym mózgu, dostarczając czystej i bezwysiłkowej energii."
      )
    )
  }

  val methodologySections = remember {
    listOf(
      DetailSection(
        id = "met_1",
        title = "1. Piana z Mleka: Epidemiologia Żywieniowa",
        subtitle = "Ankiety FFQ to fundament z piasku",
        bulletPoints = listOf(
          "Czy pamiętasz dokładnie ile razy zjadłeś pół szklanki borówek w przeciągu ostatnich 365 dni? Epidemiolodzy żywieniowi zakładają, że tak.",
          "Głównym narzędziem oficjalnych zaleceń są ankiety FFQ (Food Frequency Questionnaires).",
          "Ludzka pamięć jest wyjątkowo zawodna, a dodatkowo badani podświadomie kłamią: zaniżają spożycie produktów 'niezdrowych' (chipsy, mięso) o ponad 30-50%, a zawyżają spożycie warzyw i owoców.",
          "Ankiety te są zbierane niezwykle rzadko – np. 5 razy w ciągu 15 lat, po czym naukowcy przypisują im bezpośredni wpływ na rozwój demencji dziesięć lat później!"
        ),
        shockingTakeaway = "SZOKUJĄCY FAKT: Aż 46% ludzi przyznaje w badaniach, że nie jest w stanie wypełnić ankiet żywieniowych szczerze i dokładnie. Cała współczesna piramida żywieniowa została oparta właśnie na takich ankietach!"
      ),
      DetailSection(
        id = "met_2",
        title = "2. Korelacja to NIE Przyczynowość",
        subtitle = "Błędy precla i czynniki zakłócające",
        bulletPoints = listOf(
          "Jeśli ludzie jedzący darmowych precli w pubach chorują częściej na marskość wątroby, epidemiolog ogłosi: 'Precle niszczą wątrobę!'",
          "W rzeczywistości pomija się czynnik zakłócający (confounder): picie alkoholu, które towarzyszy jedzeniu precli.",
          "Ludzie jedzący czerwone mięso w latach 80. i 90. ogólnie mniej dbali o zdrowie (częściej palili, rzadziej ćwiczyli, jedli dużo cukru). Epidemiologia oskarżyła mięso, a nie styl życia.",
          "Przeciwnie: osoby jedzące dużo uprawianej według zaleceń diety śródziemnomorskiej to z reguły bogaci, dobrze wykształceni ludzie, którzy zdrowo śpią i unikają stresu."
        ),
        shockingTakeaway = "SZOKUJĄCY FAKT: Badania obserwacyjne mogą służyć jedynie do stawiania hipotez, nigdy do udowadniania, że czerwone mięso, tłuszcz nasycony czy jakiekolwiek inne jedzenie bezpośrednio wywołuje raka lub zawały."
      ),
      DetailSection(
        id = "met_3",
        title = "3. Kryteria Bradforda Hilla: Co jest Nauką?",
        subtitle = "Statystyczny szum w badaniach żywieniowych",
        bulletPoints = listOf(
          "Sir Austin Hill stworzył w 1965 roku kryteria analizy przyczynowej. Pierwszym warunkiem jest siła korelacji (Relative Risk - RR).",
          "W badaniach nad paleniem tytoniu i rakiem płuc ryzyko względne (RR) wynosi od 8.0 do 32.0 (palacz choruje 3200% częściej). Związek jest oczywisty.",
          "Nauka przyjmuje, że badanie obserwacyjne powinno wykazać RR powyżej 2.0 (czyli wzrost ryzyka o 100%), aby brać je pod uwagę pod kątem przyczynowym.",
          "Większość harvardzkich badań żywieniowych wykazuje RR na poziomie zaleznie 1.1 do 1.3 (np. czerwone mięso i serce to RR na poziomie... 1.03!)."
        ),
        shockingTakeaway = "SZOKUJĄCY FAKT: Ryzyko rzędu 1.03 (3% wzrostu) jest w statystyce zwykłym szumem pomiarowym, niemożliwym do oddzielenia od błędu ankiety. Mimo to mądrze brzmiące nagłówki krzyczą o zabójczej diecie."
      ),
      DetailSection(
        id = "met_4",
        title = "4. Przebiegły Eksperyment Aniczkowa na Królikach",
        subtitle = "Błąd roślinożercy i fobia cholesterolowa",
        bulletPoints = listOf(
          "W 1913 r. Nikołaj Aniczkow wywołał miażdżycę u królików, karmiąc je czystym, wyizolowanym cholesterolem zwierzęcym.",
          "Eksperyment stał się fundamentem teorii, że cholesterol zatyka tętnice.",
          "Problem: Króliki są ścisłymi roślinożercami (herbivores). Ich układ pokarmowy i enzymy nigdy nie ewoluowały do metabolizowania pokarmów zwierzęcych.",
          "Gdy badanie powtórzono na psach, szczurach i innych wszystkożercach (takich jak my), ich organizmy poradziły sobie z nadmiarem bez problemów, a naczynia pozostały całkowicie czyste."
        ),
        shockingTakeaway = "SZOKUJĄCY FAKT: Cała krucjata przeciwko jajkom i nasyconym tłuszczom zwierzęcym opiera się na fakcie, że roślinożerny królik zachorował po jedzeniu pokarmu dla drapieżników!"
      ),
      DetailSection(
        id = "met_5",
        title = "5. Ciemna Strona Antyoksydantów i Baza ORAC",
        subtitle = "Hormeza, ksenobiotyki i fiasko probówki",
        bulletPoints = listOf(
          "W latach 90. podawano ludziom wyizolowane przeciwutleniacze (witaminy C, E, beta-karoten) w rzetelnych RCT. Liczba zgonów i nowotworów u pacjentów... wzrosła!",
          "Słynny ranking ORAC (zdolność antyoksydacyjna w próbówce) wywołał marketingową obsesję na punkcie superfoods.",
          "W 2012 roku Departament Rolnictwa USA (USDA) oficjalnie usunął bazę danych ORAC, przyznając, że to co działo się w probówce, kompletnie nie działa w ludzkim ciele.",
          "Roślinne polifenole i flawonoidy są traktowane przez wątrobę jako ksenobiotyki (obce toksyny). Układ odpornościowy dąży do ich wyeliminowania.",
          "Rzekome korzyści ('stymulacja') wynikają z reakcji obronnej ciała na te łagodne trucizny roślinne, a nie z ich bezpośredniego oczyszczania."
        ),
        shockingTakeaway = "SZOKUJĄCY FAKT: Mózg nie potrzebuje bombardowania sztucznymi polifenolami roślinnymi. Potrzebuje najczystszego biologicznie paliwa (ketonów) i wysokiej jakości mikroelementów z tłuszczów zwierzęcych."
      )
    )
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(DeepDarkBlue)
  ) {
    // Custom App Bar Header (Matches Design HTML)
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .background(Color.White)
        .padding(horizontal = 20.dp, vertical = 16.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        // Author Badge Initials Container
        Box(
          contentAlignment = Alignment.Center,
          modifier = Modifier
            .size(40.dp)
            .clip(RoundedCornerShape(50))
            .background(EnergyAmber)
        ) {
          Text(
            text = "GE",
            fontSize = 15.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White
          )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
          Text(
            text = "Mind Fuel",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = CleanWhite,
            modifier = Modifier.testTag("app_title")
          )
          Text(
            text = "PROTOKÓŁ DR. EDE",
            fontSize = 9.sp,
            fontWeight = FontWeight.ExtraBold,
            color = SoftGray,
            letterSpacing = 1.sp
          )
        }
      }

      // Elegant menu button or actions placeholder
      Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
          .size(40.dp)
          .clip(RoundedCornerShape(50))
          .background(Color(0xFFF1F5F9))
          .clickable {}
      ) {
        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center
        ) {
          Box(modifier = Modifier.size(width = 16.dp, height = 2.dp).background(SoftGray))
          Spacer(modifier = Modifier.height(3.dp))
          Box(modifier = Modifier.size(width = 16.dp, height = 2.dp).background(SoftGray))
        }
      }
    }

    // Tab Controller (Thematic switch buttons)
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 12.dp)
        .clip(RoundedCornerShape(16.dp))
        .background(Color.White)
        .border(BorderStroke(1.dp, DarkCardBorder), RoundedCornerShape(16.dp))
        .testTag("tab_container")
    ) {
      Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
          .weight(1f)
          .height(44.dp)
          .clip(RoundedCornerShape(14.dp))
          .background(if (activeTab == NavigationTab.BIOCHEMISTRY) EnergyAmber else Color.Transparent)
          .clickable { activeTab = NavigationTab.BIOCHEMISTRY }
          .testTag("tab_biochemistry")
      ) {
        Text(
          text = "🧬 Biochemia i Paliwo",
          fontSize = 12.sp,
          fontWeight = FontWeight.Bold,
          color = if (activeTab == NavigationTab.BIOCHEMISTRY) Color.White else CleanWhite
        )
      }
      Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
          .weight(1f)
          .height(44.dp)
          .clip(RoundedCornerShape(14.dp))
          .background(if (activeTab == NavigationTab.METHODOLOGY) EnergyAmber else Color.Transparent)
          .clickable { activeTab = NavigationTab.METHODOLOGY }
          .testTag("tab_methodology")
      ) {
        Text(
          text = "🔬 Krytyka Nauki",
          fontSize = 12.sp,
          fontWeight = FontWeight.Bold,
          color = if (activeTab == NavigationTab.METHODOLOGY) Color.White else CleanWhite
        )
      }
    }

    val currentList = if (activeTab == NavigationTab.BIOCHEMISTRY) biochemistrySections else methodologySections

    // High performance scrolling area containing all app elements
    LazyColumn(
      modifier = Modifier
        .fillMaxWidth()
        .weight(1f),
      contentPadding = PaddingValues(bottom = 24.dp),
      verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      // 1. Motivation Hero Intro Banner (High-fidelity design translation)
      item {
        MotivationHeroCard()
      }

      // 2. Active Metabolic Simulator
      item {
        MetabolismSimulator(
          mode = simMode,
          onModeChange = { simMode = it }
        )
      }

      // 3. Tab Section Title Header
      item {
        Text(
          text = if (activeTab == NavigationTab.BIOCHEMISTRY) "🧬 DOKŁADNE ZAGADNIENIA BIOLOGICZNE:" else "🔬 ABSURDY WSPÓŁCZESNEJ METODOLOGII:",
          fontSize = 11.sp,
          fontWeight = FontWeight.ExtraBold,
          color = SoftGray,
          modifier = Modifier.padding(horizontal = 24.dp, vertical = 4.dp),
          letterSpacing = 0.5.sp
        )
      }

      // 4. Detailed Sections Cards List
      items(currentList) { section ->
        val isExpanded = expandedSectionId == section.id
        val borderAlpha by animateFloatAsState(if (isExpanded) 1f else 0.15f, label = "borderAlpha")
        val borderColor = if (isExpanded) EnergyAmber.copy(borderAlpha) else DarkCardBorder.copy(borderAlpha)

        CustomBorderCard(
          shape = RoundedCornerShape(24.dp),
          color = DarkCardBg,
          borderColor = borderColor,
          borderWidth = 1.dp,
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable {
              expandedSectionId = if (isExpanded) null else section.id
            }
            .testTag("section_card_${section.id}")
        ) {
          Column(modifier = Modifier.padding(20.dp)) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.fillMaxWidth()
            ) {
              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = section.title,
                  fontSize = 15.sp,
                  fontWeight = FontWeight.Bold,
                  color = if (isExpanded) EnergyAmber else CleanWhite,
                  lineHeight = 18.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                  text = section.subtitle,
                  fontSize = 11.sp,
                  color = SoftGray
                )
              }
              // Styled chevron circle
              Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                  .size(28.dp)
                  .clip(RoundedCornerShape(50))
                  .background(if (isExpanded) EnergyAmber.copy(0.1f) else Color(0xFFF1F5F9))
              ) {
                Text(
                  text = if (isExpanded) "▲" else "▼",
                  fontSize = 11.sp,
                  fontWeight = FontWeight.ExtraBold,
                  color = if (isExpanded) EnergyAmber else SoftGray
                )
              }
            }

            AnimatedVisibility(visible = isExpanded) {
              Column(modifier = Modifier.padding(top = 16.dp)) {
                Divider(color = DarkCardBorder.copy(alpha = 0.5f), modifier = Modifier.padding(bottom = 12.dp))

                section.bulletPoints.forEach { pt ->
                  Row(modifier = Modifier.padding(bottom = 10.dp)) {
                    Text(
                      text = "•",
                      color = KetoneCyan,
                      fontSize = 18.sp,
                      modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(
                      text = pt,
                      fontSize = 13.sp,
                      color = CleanWhite.copy(alpha = 0.85f),
                      lineHeight = 19.sp
                    )
                  }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Diagnostic Alert takeaway styling
                Box(
                  modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(BrainPink.copy(alpha = 0.05f))
                    .border(BorderStroke(1.dp, BrainPink.copy(alpha = 0.15f)), RoundedCornerShape(16.dp))
                    .padding(14.dp)
                ) {
                  Row(verticalAlignment = Alignment.Top) {
                    Icon(
                      imageVector = Icons.Default.Warning,
                      contentDescription = "Alert",
                      tint = BrainPink,
                      modifier = Modifier
                        .size(18.dp)
                        .padding(top = 1.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                      text = section.shockingTakeaway,
                      fontSize = 12.sp,
                      fontWeight = FontWeight.Bold,
                      color = BrainPink,
                      lineHeight = 16.sp
                    )
                  }
                }
              }
            }
          }
        }
      }
    }
  }
}

@Composable
fun MotivationHeroCard() {
  CustomBorderCard(
    shape = RoundedCornerShape(26.dp),
    color = Color.White,
    borderColor = DarkCardBorder,
    borderWidth = 1.dp,
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 6.dp)
  ) {
    Column(modifier = Modifier.padding(20.dp)) {
      // Physiology Badge
      Box(
        modifier = Modifier
          .clip(RoundedCornerShape(50))
          .background(BrainPink.copy(alpha = 0.1f))
          .padding(horizontal = 12.dp, vertical = 4.dp)
      ) {
        Text(
          text = "FIZJOLOGIA UMYSŁU",
          fontSize = 10.sp,
          fontWeight = FontWeight.ExtraBold,
          color = BrainPink,
          letterSpacing = 0.5.sp
        )
      }
      Spacer(modifier = Modifier.height(10.dp))

      // Main header with inline highlighted styling exactly matching design
      Text(
        text = buildAnnotatedString {
          append("Twoja siła woli to ")
          withStyle(
            SpanStyle(
              color = EnergyAmber,
              fontWeight = FontWeight.Bold,
              textDecoration = TextDecoration.Underline
            )
          ) {
            append("nie problem")
          }
          append(". To biologia.")
        },
        fontSize = 21.sp,
        fontWeight = FontWeight.ExtraBold,
        color = CleanWhite,
        lineHeight = 24.sp
      )
      Spacer(modifier = Modifier.height(8.dp))

      // Description text
      Text(
        text = "Twój mózg jest zakładnikiem insuliny. Cukier to nie nagroda — to stan wyjątkowy dla pojedynczych neuronów.",
        fontSize = 13.sp,
        color = SoftGray,
        lineHeight = 18.sp
      )
      Spacer(modifier = Modifier.height(12.dp))

      // Soft Indigo inner info box
      CustomBorderCard(
        shape = RoundedCornerShape(16.dp),
        color = EnergyAmber.copy(alpha = 0.04f),
        borderColor = EnergyAmber.copy(alpha = 0.12f),
        borderWidth = 1.dp,
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier.padding(12.dp),
          verticalAlignment = Alignment.Top
        ) {
          Text(text = "⚡", fontSize = 16.sp, modifier = Modifier.padding(top = 1.dp))
          Spacer(modifier = Modifier.width(8.dp))
          Column {
            Text(
              text = "KETOZA TERAPEUTYCZNA",
              fontSize = 10.sp,
              fontWeight = FontWeight.ExtraBold,
              color = EnergyAmber,
              letterSpacing = 0.5.sp
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
              text = "Naprawa pomp sodowo-potasowych w błonach neuronów skutecznie wycisza chaos metaboliczny i lęki.",
              fontSize = 11.sp,
              color = CleanWhite.copy(0.8f),
              lineHeight = 14.sp
            )
          }
        }
      }
    }
  }
}

@Composable
fun MetabolismSimulator(
  mode: SimulatorMode,
  onModeChange: (SimulatorMode) -> Unit
) {
  // Metric changes are dynamic and animated
  val atpProgress by animateFloatAsState(
    targetValue = if (mode == SimulatorMode.GLUCOSE) 0.25f else 0.95f,
    animationSpec = tween(durationMillis = 500),
    label = "atp"
  )
  val rosProgress by animateFloatAsState(
    targetValue = if (mode == SimulatorMode.GLUCOSE) 0.85f else 0.12f,
    animationSpec = tween(durationMillis = 500),
    label = "ros"
  )
  val insulinProgress by animateFloatAsState(
    targetValue = if (mode == SimulatorMode.GLUCOSE) 0.9f else 0.02f,
    animationSpec = tween(durationMillis = 500),
    label = "insulin"
  )

  CustomBorderCard(
    shape = RoundedCornerShape(26.dp),
    color = Color.White,
    borderColor = DarkCardBorder,
    borderWidth = 1.dp,
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 6.dp)
      .testTag("simulator_card")
  ) {
    Column(modifier = Modifier.padding(20.dp)) {
      Text(
        text = "SYMULATOR METABOLIZMU MÓZGU 🔬",
        fontSize = 12.sp,
        fontWeight = FontWeight.ExtraBold,
        color = SoftGray,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth(),
        letterSpacing = 0.5.sp
      )
      Spacer(modifier = Modifier.height(12.dp))

      // Sugar Mode vs Ketone Mode Toggles
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        // Sugar Mode button styled with soft Rose alert aesthetics
        Button(
          onClick = { onModeChange(SimulatorMode.GLUCOSE) },
          colors = ButtonDefaults.buttonColors(
            containerColor = if (mode == SimulatorMode.GLUCOSE) BrainPink.copy(alpha = 0.1f) else Color(0xFFF1F5F9),
            contentColor = if (mode == SimulatorMode.GLUCOSE) BrainPink else SoftGray
          ),
          border = BorderStroke(1.dp, if (mode == SimulatorMode.GLUCOSE) BrainPink.copy(0.3f) else Color.Transparent),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier
            .weight(1f)
            .height(40.dp)
            .testTag("sim_glucose_btn")
        ) {
          Text(text = "Cukier / Glukoza", fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }

        // Ketone Mode button styled with clean Emerald diagnostics
        Button(
          onClick = { onModeChange(SimulatorMode.KETONES) },
          colors = ButtonDefaults.buttonColors(
            containerColor = if (mode == SimulatorMode.KETONES) KetoneCyan.copy(alpha = 0.1f) else Color(0xFFF1F5F9),
            contentColor = if (mode == SimulatorMode.KETONES) KetoneCyan else SoftGray
          ),
          border = BorderStroke(1.dp, if (mode == SimulatorMode.KETONES) KetoneCyan.copy(0.3f) else Color.Transparent),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier
            .weight(1f)
            .height(40.dp)
            .testTag("sim_ketones_btn")
        ) {
          Text(text = "Ketony (KETO bypass)", fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Dynamic Metrics Bars
      MetricRow(
        label = "Wydajność Energii (ATP Mózgu):",
        valueText = if (mode == SimulatorMode.GLUCOSE) "Słabe 25%" else "Ultra-Wydajne 95%",
        progress = atpProgress,
        color = if (mode == SimulatorMode.GLUCOSE) BrainPink else KetoneCyan
      )

      Spacer(modifier = Modifier.height(12.dp))

      MetricRow(
        label = "Zagrożenie Stresem Oksydacyjnym (ROS):",
        valueText = if (mode == SimulatorMode.GLUCOSE) "Krytyczne 85% !" else "Bezpieczne 12%",
        progress = rosProgress,
        color = if (mode == SimulatorMode.GLUCOSE) BrainPink else KetoneCyan
      )

      Spacer(modifier = Modifier.height(12.dp))

      MetricRow(
        label = "Blokada Oporności Insuliny (Głód):",
        valueText = if (mode == SimulatorMode.GLUCOSE) "90% (Mózg Głoduje!)" else "2% (Całkowity Bypass)",
        progress = insulinProgress,
        color = if (mode == SimulatorMode.GLUCOSE) BrainPink else KetoneCyan
      )

      Spacer(modifier = Modifier.height(16.dp))

      // Diagnosis explanation summary
      val backgroundMState = if (mode == SimulatorMode.GLUCOSE) BrainPink.copy(alpha = 0.05f) else KetoneCyan.copy(alpha = 0.05f)
      val borderMState = if (mode == SimulatorMode.GLUCOSE) BrainPink.copy(alpha = 0.15f) else KetoneCyan.copy(alpha = 0.15f)

      Box(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(16.dp))
          .background(backgroundMState)
          .border(BorderStroke(1.dp, borderMState), RoundedCornerShape(16.dp))
          .padding(14.dp)
      ) {
        Column {
          val textTitle = if (mode == SimulatorMode.GLUCOSE) {
            "🔴 STAN KRYZYSU ENERGETYCZNEGO (Głód komórkowy)"
          } else {
            "🟢 STAN ULTRA-WYDAJNEGO ODŻYWIENIA"
          }

          val textBody = if (mode == SimulatorMode.GLUCOSE) {
            "Przez insulinooporność mózg traci zdolność spalania cukru. Działa głównie powolny Silnik G (Glikoliza). Uszkodzony Kompleks I blokuje łańcuch oddechowy. Neurony głodują w chmurze niszczących wolnych rodników, destabilizując nastrój."
          } else {
            "Ciała ketonowe (BHB) zasilają bezpośrednio Silnik M (Mitochondria), wymagając zaledwie 3 łatwych kroków (zamiast 13). Omijają Kompleks I, wchodząc przez Kompleks II. Brak potrzeby insuliny leczy chroniczne niedożywienie."
          }

          Text(
            text = textTitle,
            color = if (mode == SimulatorMode.GLUCOSE) BrainPink else KetoneCyan,
            fontSize = 11.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 0.5.sp
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = textBody,
            color = CleanWhite.copy(0.9f),
            fontSize = 11.sp,
            lineHeight = 15.sp
          )
        }
      }
    }
  }
}

@Composable
fun MetricRow(
  label: String,
  valueText: String,
  progress: Float,
  color: Color
) {
  Column {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Text(text = label, fontSize = 11.sp, color = SoftGray)
      Text(text = valueText, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = color)
    }
    Spacer(modifier = Modifier.height(4.dp))
    LinearProgressIndicator(
      progress = progress,
      color = color,
      trackColor = Color(0xFFF1F5F9), // Slate-100 placeholder track
      modifier = Modifier
        .fillMaxWidth()
        .height(6.dp)
        .clip(RoundedCornerShape(3.dp))
    )
  }
}

// Gorgeous Systemic Bottom Navigation Bar
@Composable
fun BottomNavigationBar() {
  Surface(
    modifier = Modifier
      .fillMaxWidth()
      .background(Color.White)
      .navigationBarsPadding(),
    border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
    color = Color.White
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 24.dp, vertical = 12.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      BottomNavTab(icon = "🏠", label = "Home", active = true)
      BottomNavTab(icon = "🧪", label = "Labs", active = false)
      BottomNavTab(icon = "📊", label = "Track", active = false)
      BottomNavTab(icon = "👤", label = "Profile", active = false)
    }
  }
}

@Composable
fun BottomNavTab(icon: String, label: String, active: Boolean) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier
      .padding(vertical = 4.dp)
      .alpha(if (active) 1.0f else 0.40f)
      .clickable(enabled = !active) {}
  ) {
    Text(text = icon, fontSize = 20.sp)
    Spacer(modifier = Modifier.height(2.dp))
    Text(
      text = label.uppercase(),
      fontSize = 9.sp,
      fontWeight = FontWeight.ExtraBold,
      color = if (active) EnergyAmber else CleanWhite
    )
  }
}

// Convenient helper extension to support customizable border on cards
@Composable
fun CustomBorderCard(
  modifier: Modifier = Modifier,
  shape: RoundedCornerShape,
  color: Color,
  borderColor: Color = Color.Transparent,
  borderWidth: androidx.compose.ui.unit.Dp = 0.dp,
  content: @Composable () -> Unit
) {
  Box(
    modifier = modifier
      .clip(shape)
      .background(color)
      .then(
        if (borderWidth > 0.dp) {
          Modifier.border(width = borderWidth, color = borderColor, shape = shape)
        } else {
          Modifier
        }
      )
  ) {
    content()
  }
}
