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
        var currentScreen by remember { mutableStateOf(MainScreen.HOME) }
        var showInfoDialog by remember { mutableStateOf(false) }

        Scaffold(
          modifier = Modifier.fillMaxSize(),
          topBar = {
            SharedTopBar(onMenuClick = { showInfoDialog = true })
          },
          bottomBar = {
            BottomNavigationBar(
              currentScreen = currentScreen,
              onScreenChange = { currentScreen = it }
            )
          }
        ) { innerPadding ->
          Box(modifier = Modifier.padding(innerPadding)) {
            when (currentScreen) {
              MainScreen.HOME -> KetoBrainHandbookScreen()
              MainScreen.TRACK -> TrackScreen()
              MainScreen.LABS -> LabsScreen()
            }
          }

          if (showInfoDialog) {
            AlertDialog(
              onDismissRequest = { showInfoDialog = false },
              confirmButton = {
                TextButton(
                  onClick = { showInfoDialog = false },
                  modifier = Modifier.testTag("dialog_confirm_btn")
                ) {
                  Text("Rozumiem", fontWeight = FontWeight.Bold, color = EnergyAmber)
                }
              },
              title = {
                Text(
                  "Dr. Georgia Ede — O Programie",
                  fontSize = 18.sp,
                  fontWeight = FontWeight.Bold,
                  color = CleanWhite
                )
              },
              text = {
                Column {
                  Text(
                    "Dr. Georgia Ede to wybitna lekarka psychiatra wyszkolona na Harvardzie, która pioniersko bada wpływ spektrum metabolicznego i żywienia na zdrowie psychiczne.",
                    fontSize = 13.sp,
                    color = CleanWhite,
                    lineHeight = 18.sp
                  )
                  Spacer(modifier = Modifier.height(10.dp))
                  Text(
                    "Ważna informacja medyczna:\nPrezentowane treści mają charakter wyłącznie edukacyjno-informacyjny. Przed modyfikacją diety lub dawkowania leków skonsultuj się ze swoim lekarzem prowadzącym.",
                    fontSize = 12.sp,
                    color = BrainPink,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 16.sp
                  )
                }
              },
              shape = RoundedCornerShape(20.dp),
              containerColor = Color.White
            )
          }
        }
      }
    }
  }
}

enum class MainScreen {
  HOME,
  TRACK,
  LABS
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
fun StyledDivider(modifier: Modifier = Modifier) {
  Box(
    modifier = modifier
      .fillMaxWidth()
      .height(1.dp)
      .background(DarkCardBorder.copy(alpha = 0.5f))
  )
}

@Composable
fun SharedTopBar(onMenuClick: () -> Unit) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .background(Color.White)
      .statusBarsPadding()
      .padding(horizontal = 20.dp, vertical = 14.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    Row(verticalAlignment = Alignment.CenterVertically) {
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

    Box(
      contentAlignment = Alignment.Center,
      modifier = Modifier
        .size(40.dp)
        .clip(RoundedCornerShape(50))
        .background(Color(0xFFF1F5F9))
        .clickable { onMenuClick() }
        .testTag("top_menu_button")
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
}

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
        subtitle = "Pompa sodowo-potasowa i stałe zapotrzebowanie",
        bulletPoints = listOf(
          "Mózg człowieka reprezentuje jedynie 2% masy ciała, ale pochłania aż 20% do 25% energii metabolicznej całego organizmu (u dzieci sięga to nawet 50-60%).",
          "Aż 60-70% całkowitej puli ATP komórek nerwowych jest nieprzerwanie zużywane na zasilanie pomp sodowo-potasowych (Na+/K+-ATPase), przywracających polaryzację napięcia synaptycznego błon komórkowych (ok. -70 mV).",
          "Każdy pojedynczy neuron potrzebuje około 4 miliardów cząsteczek ATP na sekundę. Bez tego traci integralność elektrochemiczną, wyzwalając chaos.",
          "Dr Ede dowodzi, że zaburzenia psychiczne (mania, lęk, depresja lekooporna, ADHD, choroba dwubiegunowa) mają wspólne metaboliczne serce: chroniczny deficyt wytwarzania potencjału energetycznego na poziomie mitochondriów.",
          "W warunkach insulinooporności bariera krew-mózg traci drożność i neurony głodują, pomimo wysokiego poziomu glukozy (cukru) panującego w krwiobiegu obwodowym."
        ),
        shockingTakeaway = "SZOKUJĄCY FAKT: Najmniejsze spadki napięcia energetycznego skutkują natychmiastowym chaosem w neuroprzekaźnikach – stąd nagłe fale lęków i wahania nastroju, wywołane biologicznym głodem komórki."
      ),
      DetailSection(
        id = "bio_2",
        title = "2. Neurony, Glej i Izolacja Kabli",
        subtitle = "Zintegrowany ekosystem i rola myeliny",
        bulletPoints = listOf(
          "W czaszce znajduje się ok. 85-86 miliardów neuronów oraz wspierająca ich potężna armia ochronnych komórek glejowych (najważniejsze to oligodendrocyty, astrocyty i mikroglej).",
          "Oligodendrocyty owijają czułe aksony neuronów osłonkami mielinowymi (do 80 warstw lipidów i cholesterolu). Ta doskonała izolacja izoluje impuls elektryczny, przyspieszając jego ruch do 320 km/h.",
          "Astrocyty działają jak most logistyczny – pobierają glukozę z krwi i wstępnie przetwarzają ją w mleczan, by precyzyjnie karmić neurony (cykl wahadłowy mleczanu ANLS).",
          "Mikroglej to komórki odpornościowe mózgu. Pod wpływem nadmiaru cukru i stanów zapalnych aktywują się do niszczącego fenotypu M1, uwalniając cytokiny zapalne (TNF-α, IL-1β, IL-6).",
          "Niszczenie lipidowo-cholesterolowej otoczki mielinowej spowalnia i rozprasza sygnały elektryczne, co klinicznie objawia się gęstą mgłą mózgową i apatią."
        ),
        shockingTakeaway = "SZOKUJĄCY FAKT: Mózg potrzebuje zdrowych nasyconych tłuszczów zwierzęcych i cholesterolu do utrzymania 80 warstw osłonki mielinowej. Dieta ultra-niskotłuszczowa powoli pozbawia go tej krytycznej izolacji!"
      ),
      DetailSection(
        id = "bio_3",
        title = "3. Silnik G (Glikoliza) vs Silnik M (Mitochondria)",
        subtitle = "Biochemiczne szlaki pozyskiwania ATP",
        bulletPoints = listOf(
          "Silnik G (Glikoliza) zachodzi w płynie komórkowym (cytoplazmie) bez tlenu. To proces prymitywny, dający zaledwie 2 cząsteczki ATP z cząsteczki glukozy.",
          "Silnik M (Mitochondrialny) – zaawansowana praca tlenowa w mitochondriach. Elektrony przemieszczają się przez łańcuch oddechowy (ETC), generując aż 34-36 czystych cząsteczek ATP.",
          "Stabilne spalanie tlenowe w Silniku M wymaga obecności kluczowych kofaktorów: magnezu, żelaza organicznego oraz witamin z grupy B (B1, B2, B3, B5, B7).",
          "Zdrowie błon mitochondrialnych opiera się na kardiolipinie. Spożywanie przetworzonych olejów roślinnych niszczy ten strukturę, powodując ucieczkę elektronów i załamanie Silnika M."
        ),
        shockingTakeaway = "SZOKUJĄCY FAKT: Opieranie metabolizmu mózgu wyłącznie na Silniku G (z powodu uszkodzenia mitochondriów) przypomina palenie w piecu stertą papierowych gazet – daje szybki ogień i masę popiołu, zamiast stabilnego ciepła dębowego drewna."
      ),
      DetailSection(
        id = "bio_4",
        title = "4. Przekleństwo Glukozy i Blokada Insuliny",
        subtitle = "Cerebral Glucose Hypometabolism",
        bulletPoints = listOf(
          "Aby neurony mogły skutecznie przetwarzać glukozę, niezbędna jest wysoka wrażliwość receptorów dla insuliny (IR) znajdujących się na synapsach i obrzeżach bariery krew-mózg.",
          "Chroniczna dominacja węglowodanów w diecie (chleby, kasze, cukry proste, nadmiar fruktozy) prowadzi do ogłuszenia i redukcji tych receptorów (tzw. down-regulation).",
          "Wychwyt glukozy ulega zablokowaniu. Powstaje mózgowa insulinooporność: cukier zalega w krwi obwodowej, ale komórki mózgowe dosłownie umierają z głodu.",
          "Dodatkowo wolne rodniki z nadmiaru cukru wchodzą w reakcje glikacji z białkami, generując zabójcze cząsteczki AGEs (karmelizacja hipokampa prowadząca do Alzheimera/Cukrzycy typu 3)."
        ),
        shockingTakeaway = "SZOKUJĄCY FAKT: Osoba ze świetnym cukrem we krwi może mieć potężną mózgową insulinooporność. Mózg woła o energię wzmagając stany lękowe, panikę i napady głodu mające wymusić wyrzut kortyzolu."
      ),
      DetailSection(
        id = "bio_5",
        title = "5. Ketony jako Rewolucyjny Bypass",
        subtitle = "Ominięcie Kompleksu I i ocalenie neuronów",
        bulletPoints = listOf(
          "Podczas braku węglowodanów poziom insuliny spada, uwalniając lipolizę. Wątroba z lipidów produkuje ciała ketonowe: beta-hydroksymaślan (BHB) i acetooctan.",
          "Przekształcenie glukozy w Acetyl-CoA wymaga aż 13 skomplikowanych reakcji enzymatycznych. Przekształcenie ketonu (BHB) w to samo paliwo wymaga jedynie 3 prostych kroków!",
          "Większość patologii psychicznych (bipolar, schizofrenia, depresja lekooporna) wykazuje wrodzone lub nabyte uszkodzenia Kompleksu I w mitochondrialnym łańcuchu oddechowym.",
          "Podczas gdy glukoza ma zablokowaną drogę przez uszkodzony Kompleks I, ketony (BHB) dostarczają elektrony przez FAD bezpośrednio do Kompleksu II, całkowicie omijając ten krytyczny problem!",
          "Dodatkowo ketony nie potrzebują insuliny do wniknięcia i spalenia w komórkach mózgu, nasycając neurony stabilnym, czystym prądem."
        ),
        shockingTakeaway = "SZOKUJĄCY FAKT: Ciała ketonowe działają jak doskonały instalator biologiczny, dając natychmiastowe paliwo umysłowi omijając uszkodzone i oporne na insulinę tory glukozy!"
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
          "Większość wytycznych promujących unikanie tłuszczów zwierzęcych bazuje na badaniach obserwacyjnych korzystających z ankiet FFQ (Food Frequency Questionnaires).",
          "Te kwestionariusze pytają ludzi np. 'ile razy zjadłeś porcję borówek, brukselki lub boczku w ciągu ostatnich 365 dni?'. Zakłada to perfekcyjną, nierealną pamięć u ludzi.",
          "Dowiedziono, że badani oszukują w ankietach (ani. social desirability bias): o 30-50% zaniżają spożycie produktów określanych jako 'niezdrowe' (tłuszcz, mięso, chipsy), a zawyżają spożycie warzyw i sałatek.",
          "Kwestionariusze zbierane są skrajnie rzadko (np. 4 razy w ciągu 20 lat). Naukowe modele matematyczne po prostu zakładają, że nawyki te były idealnie stałe."
        ),
        shockingTakeaway = "SZOKUJĄCY FAKT: Aż 46% osób przyznaje, że kłamie lub strzela w ankietach żywieniowych. Współczesna piramida żywieniowa spoczywa w całości na tych zawodnych sformułowaniach!"
      ),
      DetailSection(
        id = "met_2",
        title = "2. Korelacja to NIE Przyczynowość",
        subtitle = "Błędy precla i Healthy User Bias",
        bulletPoints = listOf(
          "W epidemiologii nagminnie myli się korelację statystyczną z realnym związkiem przyczynowo-skutkowym. Klasyczny przykład to 'błąd precla'.",
          "Jeśli badani jedzący słone precle w pubach mają częściej marskość wątroby, epidemiolog ogłosi, że to precle niszczą wątrobę, pomijając całkowicie potężne ilości alkoholu wypijanego z tymi preclami.",
          "W badaniach nad czerwonym mięsem wystąpił potężny błąd zdrowego użytkownika (Healthy User Bias). W latach 80. mięso zostało oskarżone o powodowanie chorób.",
          "W efekcie osoby z wyższych warstw, dbające o zdrowie, przestały jeść mięso. Z kolei osoby jedzące mięso częściej piły alkohol, paliły, nie ćwiczyły i jadły śmieciowe węglowodany.",
          "Współczesna nauka oskarża mięso, ignorując fakt, że w tamtych latach jedzenie mięsa było po prostu markerem fatalnego stylu życia badanych osób."
        ),
        shockingTakeaway = "SZOKUJĄCY FAKT: Badania obserwacyjne potrafią tworzyć jedynie hipotezy naukowe, nigdy nie udowodnią, że tłuszcz nasycony bezpośrednio zatyka tętnice człowieka."
      ),
      DetailSection(
        id = "met_3",
        title = "3. Kryteria Bradforda Hilla: Co jest Nauką?",
        subtitle = "Szum statystyczny w badaniach żywienia",
        bulletPoints = listOf(
          "W 1965 roku sir Austin Bradford Hill stworzył 9 kryteriów pozwalających rozróżnić korelację od przyczynowości. Najważniejsza jest siła powiązania (Relative Risk - RR).",
          "W badaniach nad tytoniem i rakiem płuc ryzyko względne (RR) wynosiło od 10.0 do 30.0 (palacze chorują 1000%-3000% częściej). Związek jest jasny.",
          "W rzetelnej metodologii naukowej przyjmuje się, że badanie obserwacyjne powinno wykazać RR powyżej 2.0 (wzrost ryzyka o 100%), aby brać je pod uwagę przyczynowo.",
          "Większość harvardzkich nagłówków żywieniowych wykazuje RR na poziomie zaledwie 1.05 do 1.2 (rzekomy wzrost o 5%-20%), co w medycynie jest jedynie szumem pomiarowym."
        ),
        shockingTakeaway = "SZOKUJĄCY FAKT: Ryzyko rzędu 1.15 jest statystycznie niemożliwe do oddzielenia od błędu ankiety, mimo to media prezentują je jako dowód na szkodliwość masła czy jaj."
      ),
      DetailSection(
        id = "met_4",
        title = "4. Przebiegły Eksperyment Aniczkowa na Królikach",
        subtitle = "Błąd roślinożercy i początki lipidowego terroru",
        bulletPoints = listOf(
          "W 1913 roku Nikołaj Aniczkow wywołał miażdżycę u królików, podając im w diecie potężne dawki wyizolowanego cholesterolu pochodzenia zwierzęcego.",
          "To badanie stało się filarem teorii cholesterolowej zabraniającej jedzenia nasyconych tłuszczów.",
          "Problem: Królik to ścisły roślinożerca. Jego układ pokarmowy, enzymy wątrobowe i metabolizm nie mają biochemicznych narzędzi do przetwarzania pokarmów zwierzęcych.",
          "Gdy ten sam eksperyment powtórzono na świniach, szczurach, psach i innych organizmach wszystkożernych (jak człowiek), ich wątroby bez problemu zmetabolizowały cholesterol, a naczynia pozostały idealnie czyste."
        ),
        shockingTakeaway = "SZOKUJĄCY FAKT: Krucjata przeciwko jajkom i tłuszczom nasyconym bierze swój początek z faktu, że roślinożerny królik zachorował po jedzeniu pokarmu dla drapieżników!"
      ),
      DetailSection(
        id = "met_5",
        title = "5. Ciemna Strona Antyoksydantów i Baza ORAC",
        subtitle = "Hormeza, ksenobiotyki i fiasko probówki",
        bulletPoints = listOf(
          "Przez lata wmawiano nam cudowne działanie antyoksydantów roślinnych. Słynny ranking ORAC (zdolność pochłaniania wolnych rodników) napędzał rynek superfoods.",
          "W 2012 roku Departament Rolnictwa USA (USDA) oficjalnie wycofał i usunął bazę danych ORAC, przyznając, że wyniki z probówek mają się nijak do ludzkiego organizmu.",
          "Co gorsza, badania kliniczne (RCT), gdzie podawano megadawki antyoksydantów (witaminy C, E, beta-karoten), zarejestrowały u ludzi wzrost śmiertelności i raka!",
          "Rośliny bronią się przed zjedzeniem za pomocą toksyn (ksenobiotyków, polifenoli). Twoja wątroba dąży do ich eliminacji.",
          "Rzekome korzyści ('stymulacja') wynikają z reakcji obronnej ciała na te łagodne trucizny roślinne, a nie z ich bezpośredniego działania oczyszczającego."
        ),
        shockingTakeaway = "SZOKUJĄCY FAKT: Mózg nie potrzebuje bombardowania ksenobiotykami roślinnymi. Pragnie stabilnego, biologicznie najczystszego paliwa (ketonów) i minerałów z tłuszczów zwierzęcych."
      )
    )
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(DeepDarkBlue)
  ) {
    // Tab Controller
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

    LazyColumn(
      modifier = Modifier
        .fillMaxWidth()
        .weight(1f),
      contentPadding = PaddingValues(bottom = 24.dp),
      verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      item {
        MotivationHeroCard()
      }

      item {
        MetabolismSimulator(
          mode = simMode,
          onModeChange = { simMode = it }
        )
      }

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
                StyledDivider(modifier = Modifier.padding(bottom = 12.dp))

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

      Text(
        text = "Twój mózg jest zakładnikiem insuliny. Cukier to nie nagroda — to stan wyjątkowy dla pojedynczych neuronów.",
        fontSize = 13.sp,
        color = SoftGray,
        lineHeight = 18.sp
      )
      Spacer(modifier = Modifier.height(12.dp))

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
        text = "WIZUALIZACJA METABOLIZMU MÓZGU 🔬",
        fontSize = 12.sp,
        fontWeight = FontWeight.ExtraBold,
        color = SoftGray,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth(),
        letterSpacing = 0.5.sp
      )
      Spacer(modifier = Modifier.height(12.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
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
      trackColor = Color(0xFFF1F5F9),
      modifier = Modifier
        .fillMaxWidth()
        .height(6.dp)
        .clip(RoundedCornerShape(3.dp))
    )
  }
}

@Composable
fun TrackScreen() {
  var selectedProtocol by remember { mutableStateOf("keto") }
  var selectedTransition by remember { mutableStateOf(0) }

  val protocolFoods = mapOf(
    "paleo" to listOf("🥑 Awokado", "🥩 Wołowina", "🐟 Dziki Łosoś", "🥦 Brokuły", "🥚 Jaja Eko", "🥕 Marchew", "🫐 Jagody"),
    "keto" to listOf("🥑 Awokado", "🥩 Tłusta Wołowina", "🥓 Boczek Eko", "🧈 Masło Ghee", "🍳 Żółtka jaj", "🥥 Olej kokos", "🥬 Szpinak"),
    "carnivore" to listOf("🥩 Antrykot wołowy", "🐑 Jagnięcina", "🧈 Masło Ghee", "🍳 Żółtka jaj", "🧂 Sól morska", "💧 Woda mineralna")
  )

  val mealPlans = mapOf(
    "paleo" to listOf(
      "Śniadanie" to "Jajecznica z 3 jaj na maśle klarowanym, plasterki świeżego awokado i garstka dzikich jagód.",
      "Obiad" to "Pieczona pierś z kaczki ze słodkim ziemniakiem (batatem) oraz duszonym szpinakiem z czosnkiem.",
      "Kolacja" to "Dziki łosoś na parze skropiony oliwą z oliwek, grillowane szparagi i prosta zielona sałata."
    ),
    "keto" to listOf(
      "Śniadanie" to "Puszysty omlet żółtkowy z boczkiem, smażony na obfitej łyżce masła klarowanego.",
      "Obiad" to "Mielone kotlety wołowe z łopatki, nadziewane masłem czosnkowym, podane z pieczonym kalafiorem.",
      "Kolacja" to "Chłodny tatar wołowy z żółtkiem jaja kurzego, cebulką i oliwą z oliwek ekstra premium."
    ),
    "carnivore" to listOf(
      "Śniadanie" to "Smażony stek z antrykotu wołowego (Ribeye) z roztopionym masłem i sporą szczyptą soli morskiej.",
      "Obiad" to "Pieczone żeberka jagnięce (tłuste części), obficie posolone krystaliczną solą kłodawską.",
      "Kolacja" to "Szybki posiłek z pieczonej wątróbki cielęcej w maśle klarowanym – doskonała dawka mikroelementów."
    )
  )

  val protocolClinicalLogic = mapOf(
    "paleo" to "Etap przygotowawczy wykluczający sztuczne, przetworzone pożywienie, cukry proste, nabiał pasteryzowany oraz toksyczne oleje nasienne. To reset układu pokarmowego i eliminacja lektyn zbożowych bez wprowadzania organizmu w stan głębokiej ketozy.",
    "keto" to "Paliwo ratunkowe dla wygłodzonych neuronów. Poprzez redukcję węglowodanów do minimum, wątroba przekształca tłuszcze w ciała ketonowe (BHB), które z łatwością przekraczają barierę krew-mózg bez udziału insuliny.",
    "carnivore" to "Absolutne ultimatum dla układu immunologicznego. Wyklucza 100% roślinnych antynutrientów, szczawianów, salicylanów oraz błonnika, który może drażnić nieszczelne jelita. Doskonałe narzędzie kliniczne przy ciężkich zaburzeniach nastroju."
  )

  val activeFoods = protocolFoods[selectedProtocol] ?: emptyList()
  val activePlan = mealPlans[selectedProtocol] ?: emptyList()
  val activeLogic = protocolClinicalLogic[selectedProtocol] ?: ""

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .background(DeepDarkBlue),
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    item {
      Column {
        Text(
          text = "Protokoły Dietetyczne Dr. Ede",
          fontSize = 20.sp,
          fontWeight = FontWeight.Bold,
          color = CleanWhite
        )
        Text(
          text = "Poznaj stopnie eliminacji pokarmowej. Każdy kolejny krok wyklucza więcej potencjalnych toksyn roślinnych, dając ulgę neuronom.",
          fontSize = 13.sp,
          color = SoftGray,
          lineHeight = 18.sp
        )
      }
    }

    item {
      Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        // Quiet Paleo Toggle
        val isPaleoSelected = selectedProtocol == "paleo"
        CustomBorderCard(
          shape = RoundedCornerShape(20.dp),
          color = if (isPaleoSelected) KetoneCyan.copy(0.08f) else Color.White,
          borderColor = if (isPaleoSelected) KetoneCyan else DarkCardBorder,
          borderWidth = if (isPaleoSelected) 2.dp else 1.dp,
          modifier = Modifier
            .fillMaxWidth()
            .clickable { selectedProtocol = "paleo" }
            .testTag("protocol_paleo")
        ) {
          Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              contentAlignment = Alignment.Center,
              modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(KetoneCyan.copy(0.12f))
            ) {
              Text("🥗", fontSize = 20.sp)
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
              Text("Quiet Paleo", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = CleanWhite)
              Text("KROK 1: Stabilizacja glukozy (Bez cukru i glutenu)", fontSize = 11.sp, color = SoftGray)
            }
            if (isPaleoSelected) {
              Icon(Icons.Default.Check, contentDescription = "Wybrany", tint = KetoneCyan, modifier = Modifier.size(20.dp))
            }
          }
        }

        // Quiet Keto Toggle
        val isKetoSelected = selectedProtocol == "keto"
        CustomBorderCard(
          shape = RoundedCornerShape(20.dp),
          color = if (isKetoSelected) EnergyAmber.copy(0.08f) else Color.White,
          borderColor = if (isKetoSelected) EnergyAmber else DarkCardBorder,
          borderWidth = if (isKetoSelected) 2.dp else 1.dp,
          modifier = Modifier
            .fillMaxWidth()
            .clickable { selectedProtocol = "keto" }
            .testTag("protocol_keto")
        ) {
          Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              contentAlignment = Alignment.Center,
              modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(EnergyAmber.copy(0.12f))
            ) {
              Text("🥑", fontSize = 20.sp)
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
              Text("Quiet Keto", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = CleanWhite)
              Text("KROK 2: Głęboka ketoza (Spalanie czystego BHB)", fontSize = 11.sp, color = SoftGray)
            }
            if (isKetoSelected) {
              Icon(Icons.Default.Check, contentDescription = "Wybrany", tint = EnergyAmber, modifier = Modifier.size(20.dp))
            }
          }
        }

        // Quiet Carnivore Toggle
        val isCarnoSelected = selectedProtocol == "carnivore"
        CustomBorderCard(
          shape = RoundedCornerShape(20.dp),
          color = if (isCarnoSelected) BrainPink.copy(0.08f) else Color.White,
          borderColor = if (isCarnoSelected) BrainPink else DarkCardBorder,
          borderWidth = if (isCarnoSelected) 2.dp else 1.dp,
          modifier = Modifier
            .fillMaxWidth()
            .clickable { selectedProtocol = "carnivore" }
            .testTag("protocol_carnivore")
        ) {
          Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              contentAlignment = Alignment.Center,
              modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(BrainPink.copy(0.12f))
            ) {
              Text("🥩", fontSize = 20.sp)
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
              Text("Quiet Carnivore", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = CleanWhite)
              Text("KROK 3: Maksymalna eliminacja (Dla stanów lękowych)", fontSize = 11.sp, color = SoftGray)
            }
            if (isCarnoSelected) {
              Icon(Icons.Default.Check, contentDescription = "Wybrany", tint = BrainPink, modifier = Modifier.size(20.dp))
            }
          }
        }
      }
    }

    // Logic description card
    item {
      CustomBorderCard(
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        borderColor = DarkCardBorder,
        borderWidth = 1.dp
      ) {
        Column(modifier = Modifier.padding(20.dp)) {
          Text(
            text = "💡 DLACZEGO TEN ETAP JEST KLUCZOWY?",
            fontSize = 11.sp,
            fontWeight = FontWeight.ExtraBold,
            color = SoftGray,
            letterSpacing = 0.5.sp
          )
          Spacer(modifier = Modifier.height(10.dp))
          Text(
            text = activeLogic,
            fontSize = 13.sp,
            color = CleanWhite,
            lineHeight = 18.sp
          )
        }
      }
    }

    // Allowed Foods Card
    item {
      CustomBorderCard(
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        borderColor = DarkCardBorder,
        borderWidth = 1.dp
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Text(
            text = "🛒 REKOMENDOWANA LISTA ZAKUPÓW",
            fontSize = 11.sp,
            fontWeight = FontWeight.ExtraBold,
            color = SoftGray,
            letterSpacing = 0.5.sp
          )
          Spacer(modifier = Modifier.height(10.dp))

          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            activeFoods.take(3).forEach { food ->
              Box(
                modifier = Modifier
                  .weight(1f)
                  .clip(RoundedCornerShape(12.dp))
                  .background(Color(0xFFF1F5F9))
                  .border(BorderStroke(1.dp, Color(0xFFE2E8F0)), RoundedCornerShape(12.dp))
                  .padding(8.dp)
              ) {
                Text(
                  text = food,
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  color = CleanWhite,
                  textAlign = TextAlign.Center,
                  modifier = Modifier.fillMaxWidth()
                )
              }
            }
          }
          Spacer(modifier = Modifier.height(6.dp))
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            activeFoods.drop(3).take(4).forEach { food ->
              Box(
                modifier = Modifier
                  .weight(1f)
                  .clip(RoundedCornerShape(12.dp))
                  .background(Color(0xFFF1F5F9))
                  .border(BorderStroke(1.dp, Color(0xFFE2E8F0)), RoundedCornerShape(12.dp))
                  .padding(8.dp)
              ) {
                Text(
                  text = food,
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  color = CleanWhite,
                  textAlign = TextAlign.Center,
                  modifier = Modifier.fillMaxWidth()
                )
              }
            }
          }
        }
      }
    }

    // Sample Meal Plan Card
    item {
      CustomBorderCard(
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        borderColor = DarkCardBorder,
        borderWidth = 1.dp
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Text(
            text = "🍽️ PRZYKŁADOWY JADŁOSPIS JEDNODNIOWY",
            fontSize = 11.sp,
            fontWeight = FontWeight.ExtraBold,
            color = SoftGray,
            letterSpacing = 0.5.sp
          )
          Spacer(modifier = Modifier.height(14.dp))

          activePlan.forEachIndexed { idx, item ->
            val (mealTime, mealDesc) = item
            Row(
              verticalAlignment = Alignment.Top,
              modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
            ) {
              Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                  .size(24.dp)
                  .clip(RoundedCornerShape(50))
                  .background(EnergyAmber.copy(0.1f))
              ) {
                Text("${idx + 1}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = EnergyAmber)
              }
              Spacer(modifier = Modifier.width(10.dp))
              Column(modifier = Modifier.weight(1f)) {
                Text(text = mealTime, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = CleanWhite)
                Text(text = mealDesc, fontSize = 12.sp, color = SoftGray, lineHeight = 16.sp)
              }
            }
          }
        }
      }
    }

    // ==========================================
    // NEW INTERACTIVE TRANSITION GUIDE CARD
    // ==========================================
    item {
      CustomBorderCard(
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        borderColor = EnergyAmber,
        borderWidth = 1.dp,
        modifier = Modifier.testTag("transition_guide_card")
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Text(
            text = "🔄 PRZEWODNIK PRZEJŚĆ MIĘDZY PROTOKOŁAMI",
            fontSize = 12.sp,
            fontWeight = FontWeight.ExtraBold,
            color = EnergyAmber,
            letterSpacing = 0.5.sp
          )
          Text(
            text = "Wizualny kompas fizjologiczny dr Ede. Wybierz kierunek zmiany:",
            fontSize = 11.sp,
            color = SoftGray,
            modifier = Modifier.padding(vertical = 4.dp)
          )

          Spacer(modifier = Modifier.height(10.dp))

          // 2x2 grid of transition buttons
          Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            val directions = listOf(
              "Quiet Paleo ➔ Quiet Keto" to 0,
              "Quiet Keto ➔ Quiet Carnivore" to 1,
              "Quiet Carnivore ➔ Keto/Paleo" to 2,
              "Quiet Keto ➔ Quiet Paleo" to 3
            )

            directions.forEach { (label, idx) ->
              val isSelected = selectedTransition == idx
              Box(
                contentAlignment = Alignment.CenterStart,
                modifier = Modifier
                  .fillMaxWidth()
                  .clip(RoundedCornerShape(12.dp))
                  .background(if (isSelected) EnergyAmber.copy(0.08f) else Color(0xFFF1F5F9))
                  .border(BorderStroke(1.dp, if (isSelected) EnergyAmber else Color.Transparent), RoundedCornerShape(12.dp))
                  .clickable { selectedTransition = idx }
                  .padding(12.dp)
              ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Text(
                    text = if (isSelected) "●" else "○",
                    color = if (isSelected) EnergyAmber else SoftGray,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(end = 8.dp)
                  )
                  Text(
                    text = label,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) EnergyAmber else CleanWhite
                  )
                }
              }
            }
          }

          Spacer(modifier = Modifier.height(14.dp))
          StyledDivider(modifier = Modifier.padding(bottom = 12.dp))

          // Transition advice text block
          val adviceTitle: String
          val advicePhysiology: String
          val adviceWarning: String
          val adviceSteps: String

          when (selectedTransition) {
            0 -> {
              adviceTitle = "KIERUNEK: Quiet Paleo do Quiet Keto (Wejście w Ketozę)"
              advicePhysiology = "Fizjologia: Wątroba całkowicie wyczerpuje zapasy glikogenu w 24 do 48 godzin. Spadek poziomu insuliny odblokowuje enzym lipazę (HSL), inicjując stałą lipolizę i syntezę stabilnego beta-hydroksymaślanu (BHB) – paliwa ratunkowego dla mózgu omijającego oporność."
              adviceWarning = "Objawy/Pułapki: Grypa ketonowa (Keto Flu) - zmęczenie, ból głowy. Wbrew mitom to nie brak cukru, lecz nagła natriureza głodowa: gwałtowne usunięcie wody i niezbędnego sodu przez nerki po spadku stężeń insuliny."
              adviceSteps = "Protokół dr Ede: Pij codziennie 2 szklanki solonego bulionu kostnego lub wodę z 1/2 łyżeczki soli kamiennej. Zwiększ podaż czystego tłuszczu zwierzęcego (masło klarowane ghee, łój), unikaj forsownych treningów w pierwszych 5-7 dniach keto-adaptacji."
            }
            1 -> {
              adviceTitle = "KIERUNEK: Quiet Keto do Quiet Carnivore (Maksymalna Eliminacja)"
              advicePhysiology = "Fizjologia: Następuje 100% odcięcie substancji roślinnych. Wyeliminowane zostają wbudowane mechanizmy obronne roślin: szkodliwe lektyny, drażniący kwas fitynowy, szczawiany i salicylany. Te drobiny uszkadzają barierę jelitową i chronicznie aktywują mikroglej mózgu."
              adviceWarning = "Objawy/Pułapki: Przejściowy ból brzucha (zmiana flory bakteryjnej) oraz proces oxalate dumping (masowe pozbywanie się kryształków szczawianów z tkanek, co może przejściowo objawić się swędzeniem skóry lub pęcherzykami)."
              adviceSteps = "Protokół dr Ede: Bazuj wyłącznie na tłustych częściach wołowiny (antrykot, boczki), podrobach, ekologicznych żółtkach jaj i soli kamiennej. Całkowicie wyłącz kawę i herbatę (to bomby szczawianowe drażniące jelita). Pij wyłącznie czystą wodę."
            }
            2 -> {
              adviceTitle = "KIERUNEK: Quiet Carnivore do Keto/Paleo (Mądra Reintrodukcja)"
              advicePhysiology = "Fizjologia: Po fazie czystego Carnivore neurozapalenie ulega wygaszeniu, a bariera jelitowa szczelności. Każdy nowo wprowadzany pokarm daje natychmiastowe, nieskażone i czyste sprzężenie zwrotne o reakcjach autoimmunologicznych Twojego mózgu."
              adviceWarning = "Objawy/Pułapki: Wprowadzanie wielu pokarmów roślinnych naraz. Jeśli zjesz miskę sałatki z awokado, pomidorami i pestkami, nie namierzysz, który składnik (salicylany, histamina czy lektyny) wywołał powrót lęków bądź mgły."
              adviceSteps = "Protokół dr Ede: Wybierz jeden bezpieczny element o minimalnej toksyczności (np. czysty obrany ogórek bez nasion lub awokado). Jedz go raz dziennie przez 3 dni z rzędu. Obserwuj tętno, jasność myśli, stany lękowe i sen. Dopiero po 72h wprowadź kolejny składnik."
            }
            else -> {
              adviceTitle = "KIERUNEK: Quiet Keto do Quiet Paleo (Rozszerzenie Węglowodanów)"
              advicePhysiology = "Fizjologia: Organizm powraca do czerpania głównej energii z glukozy rosnącej w bezpiecznych źródłach bez nadmiernej hiperstymulacji trzustki. Receptory dla insuliny IR pozostają drożne i wrażliwe dzięki eliminacji cukrów rafinowanych."
              adviceWarning = "Objawy/Pułapki: Błyskawiczny powrót do glutenu, zbóż, pieczywa i słodyczy. Wywoła to gwałtowny skok insulinowy, ogólny obrzęk (retencję wody), ból głowy i niemal natychmiastowe odcięcie dopływu glukozy do neuronów."
              adviceSteps = "Protokół dr Ede: Wprowadzaj węglowodany gęste odżywczo o niskiej toksyczności z bazy Paleo: gotowane bataty, borówki, dynia piżmowa. Zawsze łącz je ze zdrowym tłuszczem nasyconym lub proteiną, co spłaszczy poposiłkową krzywą cukrową."
            }
          }

          Text(
            text = adviceTitle,
            fontSize = 12.sp,
            fontWeight = FontWeight.ExtraBold,
            color = EnergyAmber
          )
          Spacer(modifier = Modifier.height(8.dp))
          Text(text = advicePhysiology, fontSize = 11.sp, color = CleanWhite, lineHeight = 15.sp)
          Spacer(modifier = Modifier.height(6.dp))
          Text(text = adviceWarning, fontSize = 11.sp, color = BrainPink, fontWeight = FontWeight.Bold, lineHeight = 15.sp)
          Spacer(modifier = Modifier.height(6.dp))
          Text(text = adviceSteps, fontSize = 11.sp, color = KetoneCyan, fontWeight = FontWeight.Medium, lineHeight = 15.sp)
        }
      }
    }

    // Medical Elimination Graphic Card (Infographic style)
    item {
      CustomBorderCard(
        shape = RoundedCornerShape(24.dp),
        color = BrainPink.copy(0.04f),
        borderColor = BrainPink.copy(0.15f),
        borderWidth = 1.dp
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Text(
            text = "🛡️ ZASADA INTEGRALNOŚCI METABOLICZNEJ",
            fontSize = 11.sp,
            fontWeight = FontWeight.ExtraBold,
            color = BrainPink,
            letterSpacing = 0.5.sp
          )
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = "Ewolucyjnie, rośliny bronią się przed zjedzeniem za pomocą chemicznych inhibitorów i kwasów obronnych (np. szczawianów wyciągających minerały). Gdy bariera jelitowa jest nieszczelna, te czynniki bez trudu aktywują mikroglej do neurozapalenia. Im silniejsze u Ciebie stany lękowe, tym pilniej należy wejść na Krok 3 (Carnivore), aby w 100% wyciszyć te czynniki.",
            fontSize = 12.sp,
            color = CleanWhite.copy(0.9f),
            lineHeight = 16.sp
          )
        }
      }
    }
  }
}

data class LabBiomarker(
  val name: String,
  val icon: String,
  val optimalText: String,
  val warningText: String,
  val markerDesc: String,
  val whyVital: String,
  val actionHint: String
)

@Composable
fun LabsScreen() {
  val labBiomarkers = remember {
    listOf(
      LabBiomarker(
        name = "Insulina na czczo (Fasting Insulin)",
        icon = "🧬",
        optimalText = "2.0 – 5.0 uIU/ml",
        warningText = "> 6.0 uIU/ml (Słabnie wrażliwość receptorów)",
        markerDesc = "Niezwykle czuły, wczesny wskaźnik zdrowia metabolicznego. Tradycyjne laboratoria wadliwie uznają wynik do 25 uIU/ml za poprawny, co wg dr Ede jest krytycznym błędem dopuszczającym głodowanie neuronów.",
        whyVital = "Gdy czcza insulina rośnie, receptory na synapsach są stale bombardowane i ulegają niszczeniu (down-regulation). Mózg traci dopływ energii, mimo obecności glukozy.",
        actionHint = "Sugerowane działanie: Natychmiast wejdź w Krok 2 (Quiet Keto), aby dać odpocząć trzustce i wygenerować ketony omijające oporność."
      ),
      LabBiomarker(
        name = "Glukoza na czczo (Fasting Glucose)",
        icon = "🩸",
        optimalText = "70 – 85 mg/dl",
        warningText = "86 – 99 mg/dl (Stan ukryty), >100 mg/dl (Ryzyko AGEs)",
        markerDesc = "Obrazuje podstawową ilość cukru pętającego się w naczyniach krwionośnych w stanie spoczynku.",
        whyVital = "Przewlekle wysoka glukoza powoduje niszczycielski proces glikacji – wiązania cukru z białkami i tłuszczami w mózgu. Powstają Advanced Glycation End-products (AGEs), które dosłownie karmelizują mielinę i niszczą hipokamp.",
        actionHint = "Sugerowane działanie: Wyeliminuj 100% rafinowanych mąk, chleba i napojów słodzonych (przejdź na Quiet Paleo)."
      ),
      LabBiomarker(
        name = "Wskaźnik HOMA-IR (Wrażliwość metaboliczna)",
        icon = "📊",
        optimalText = "< 1.0",
        warningText = "1.0 – 1.9 (Wczesna oporność), >2.0 (Zaawansowana blokada)",
        markerDesc = "Matematyczna relacja obliczana wzorem: (Glukoza * Insulina) / 405.",
        whyVital = "HOMA-IR powyżej 1.9 to dowód na Cerebral Glucose Hypometabolism - stan, w którym komórki mózgowe głodują w morzu obfitości krwi. Jest to bezpośrednie podłoże stanów lękowych, bólów głowy i wczesnego stadium Alzheimera.",
        actionHint = "Sugerowane działanie: Jeżeli masz HOMA-IR powyżej 1.9, potrzebujesz głębokiej naprawy energetycznej pod osłoną ketonów (Quiet Keto)."
      ),
      LabBiomarker(
        name = "Stosunek TG do HDL (Trójglicerydy / HDL)",
        icon = "⚖️",
        optimalText = "< 1.5",
        warningText = "1.5 – 2.5 (Początek dyslipidemii), >2.5 (Krytyczny)",
        markerDesc = "Ilość triglicerydów podzielona przez poziom dobrego cholesterolu HDL.",
        whyVital = "Stosunek ten precyzyjnie opisuje architekturę cząsteczek cholesterolu. Niski wynik (<1.5) oznacza brak groźnych dla zdrowia małych gęstych LDL (sdLDL) oraz wyklucza stłuszczenie wątroby spowodowane fruktozą.",
        actionHint = "Sugerowane działanie: Aby gwałtownie obniżyć trójglicerydy (TG) i podnieść HDL, należy wyciąć fruktozę oraz spożywać nasycone tłuszcze zwierzęce z żółtek jaj i masła klarowanego."
      ),
      LabBiomarker(
        name = "Kwas Moczowy (Uric Acid)",
        icon = "🧪",
        optimalText = "< 5.0 mg/dl",
        warningText = "5.1 – 6.5 mg/dl (Zesztywnienie), >6.5 mg/dl (Zapalenie stawów)",
        markerDesc = "Produkt uboczny metabolizmu puryn i rozpadu wolnego ATP.",
        whyVital = "Wysoki kwas moczowy to chemiczny biomarker wyczerpania zapasów ATP w komórkach wątroby i mózgu (często z powodu fruktozy z soków owocowych) oraz marker chronicznej neuroinflamacji.",
        actionHint = "Sugerowane działanie: Całkowity zakaz spożywania syropu glukozowo-fruktozowego oraz gotowych soków i owoców wysokocukrowych."
      ),
      LabBiomarker(
        name = "Hemoglobina Glikowana (HbA1c)",
        icon = "📈",
        optimalText = "< 5.3 %",
        warningText = "5.3% – 5.6% (Podniesiona), > 5.7% (Stan dziękicukrzycowy)",
        markerDesc = "Wskaźnik obrazujący średni poziom glukozy w krążeniu z ostatnich 120 dni.",
        whyVital = "HbA1c powyżej 5.3% oznacza przewlekłe podwyższenie cukru we krwi. Im wyższa wartość HbA1c, tym silniej zachodzi proces glikacji lipidów mielinowych w mózgu i degeneracja naczyń krwionośnych.",
        actionHint = "Sugerowane działanie: Przejdź na Quiet Keto i obetnij dobową pulę węglowodanów netto poniżej 20g, by uregulować poziom HbA1c."
      ),
      LabBiomarker(
        name = "hs-CRP (C-Reactive Protein o wysokiej czułości)",
        icon = "🚨",
        optimalText = "< 0.5 mg/l",
        warningText = "0.5 – 1.0 mg/l (Przewlekły mikrostan), > 1.0 mg/l (Masywne zapalenie)",
        markerDesc = "Klasyczny wątrobowy białkowy marker ogólnoustrojowego mikrozapalenia chronicznego.",
        whyVital = "hs-CRP odzwierciedla tętniący poziom ogólnego stanu zapalnego. Przewlekłe zapalenie naczyń krwionośnych (nawet małe rzędu 0.8 mg/l) z łatwością przenika do mózgu, niszcząc barierę jelito-mózg i wywołując napady lęku.",
        actionHint = "Sugerowane działanie: Wyeliminuj oleje roślinne (słonecznikowy, rzepakowy) będące źródłem kwasu linolowego omega-6 i wejdź na Quiet Carnivore."
      )
    )
  }

  var expandedMarkerName by remember { mutableStateOf<String?>("Insulina na czczo (Fasting Insulin)") }

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .background(DeepDarkBlue),
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    item {
      Column {
        Text(
          text = "Wzorcowe Parametry Laboratoryjne",
          fontSize = 20.sp,
          fontWeight = FontWeight.Bold,
          color = CleanWhite
        )
        Text(
          text = "Redukcja szumu informacyjnego. Poznaj prawdziwe zakresy optymalne z bazy psychiatrii metabolicznej dla zdrowia neuronów.",
          fontSize = 13.sp,
          color = SoftGray,
          lineHeight = 18.sp
        )
      }
    }

    items(labBiomarkers) { biomarker ->
      val isExpanded = expandedMarkerName == biomarker.name
      val borderAlpha by animateFloatAsState(if (isExpanded) 1f else 0.15f, label = "b_alpha")
      val borderColor = if (isExpanded) KetoneCyan.copy(borderAlpha) else DarkCardBorder.copy(borderAlpha)

      CustomBorderCard(
        shape = RoundedCornerShape(24.dp),
        color = DarkCardBg,
        borderColor = borderColor,
        borderWidth = 1.dp,
        modifier = Modifier
          .fillMaxWidth()
          .clickable {
            expandedMarkerName = if (isExpanded) null else biomarker.name
          }
          .testTag("section_card_${biomarker.name.lowercase().replace(" ", "_")}")
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
          ) {
            Box(
              contentAlignment = Alignment.Center,
              modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(50))
                .background(if (isExpanded) KetoneCyan.copy(0.12f) else Color(0xFFF1F5F9))
            ) {
              Text(biomarker.icon, fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = biomarker.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = if (isExpanded) KetoneCyan else CleanWhite
              )
              Text(
                text = "Optymalny: ${biomarker.optimalText}",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = KetoneCyan
              )
            }
            Box(
              contentAlignment = Alignment.Center,
              modifier = Modifier
                .size(24.dp)
                .clip(RoundedCornerShape(50))
                .background(Color(0xFFF1F5F9))
            ) {
              Text(
                text = if (isExpanded) "▲" else "▼",
                fontSize = 10.sp,
                fontWeight = FontWeight.ExtraBold,
                color = SoftGray
              )
            }
          }

          AnimatedVisibility(visible = isExpanded) {
            Column(modifier = Modifier.padding(top = 14.dp)) {
              StyledDivider(modifier = Modifier.padding(bottom = 10.dp))

              Text(
                text = biomarker.markerDesc,
                fontSize = 12.sp,
                color = CleanWhite.copy(0.85f),
                lineHeight = 16.sp
              )
              Spacer(modifier = Modifier.height(8.dp))

              Text(
                text = "DLACZEGO TO KLUCZOWE DLA NEURONÓW?",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = SoftGray,
                letterSpacing = 0.5.sp
              )
              Text(
                text = biomarker.whyVital,
                fontSize = 12.sp,
                color = CleanWhite,
                fontWeight = FontWeight.Medium,
                lineHeight = 16.sp,
                modifier = Modifier.padding(vertical = 4.dp)
              )
              Spacer(modifier = Modifier.height(8.dp))

              Box(
                modifier = Modifier
                  .fillMaxWidth()
                  .clip(RoundedCornerShape(12.dp))
                  .background(EnergyAmber.copy(0.04f))
                  .border(BorderStroke(1.dp, EnergyAmber.copy(0.12f)), RoundedCornerShape(12.dp))
                  .padding(10.dp)
              ) {
                Text(
                  text = biomarker.actionHint,
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  color = EnergyAmber,
                  lineHeight = 15.sp
                )
              }
            }
          }
        }
      }
    }

    item {
      CustomBorderCard(
        shape = RoundedCornerShape(24.dp),
        color = BrainPink.copy(0.04f),
        borderColor = BrainPink.copy(0.15f),
        borderWidth = 1.dp
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Text(
            text = "⚠️ UWAGA O BADANIACH W POLSKICH NORMACH",
            fontSize = 11.sp,
            fontWeight = FontWeight.ExtraBold,
            color = BrainPink,
            letterSpacing = 0.5.sp
          )
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = "Standardowe zakresy laboratoryjne (tzw. referencyjne) są tworzone na podstawie średnich wyników populacji odwiedzającej te laboratoria, czyli osób zazwyczaj chorych lub zmagających się z zaburzeniami metabolicznymi. Zakres zdrowia (optymalny) jest drastycznie bardziej restrykcyjny, ponieważ odnosi się do sprawności i bezwysiłkowego, tlenowego zasilania neuronów.",
            fontSize = 12.sp,
            color = CleanWhite.copy(0.9f),
            lineHeight = 16.sp
          )
        }
      }
    }
  }
}

@Composable
fun BottomNavigationBar(
  currentScreen: MainScreen,
  onScreenChange: (MainScreen) -> Unit
) {
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
        .padding(horizontal = 24.dp, vertical = 10.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      BottomNavTab(icon = "🏠", label = "Wiedza", active = currentScreen == MainScreen.HOME, onClick = { onScreenChange(MainScreen.HOME) }, testTagId = "nav_home")
      BottomNavTab(icon = "🥗", label = "Protokoły", active = currentScreen == MainScreen.TRACK, onClick = { onScreenChange(MainScreen.TRACK) }, testTagId = "nav_track")
      BottomNavTab(icon = "🧪", label = "Wyniki", active = currentScreen == MainScreen.LABS, onClick = { onScreenChange(MainScreen.LABS) }, testTagId = "nav_labs")
    }
  }
}

@Composable
fun BottomNavTab(icon: String, label: String, active: Boolean, onClick: () -> Unit, testTagId: String) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier
      .padding(vertical = 4.dp)
      .alpha(if (active) 1.0f else 0.45f)
      .clickable { onClick() }
      .testTag(testTagId)
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
