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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.verticalScroll
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
        var currentScreen by remember { mutableStateOf(MainScreen.SIMULATOR) }
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
              MainScreen.SIMULATOR -> KetoBrainSimulatorScreen()
              MainScreen.HOME -> KetoBrainHandbookScreen()
              MainScreen.TRACK -> TrackScreen()
              MainScreen.LABS -> LabsScreen()
              MainScreen.MOTIVATION -> MotivationScreen()
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
  SIMULATOR,
  HOME,
  TRACK,
  LABS,
  MOTIVATION
}

enum class NavigationTab {
  BIOCHEMISTRY,
  METHODOLOGY,
  DIETS_CLINICAL,
  FOOD_LIBRARY,
  MED_WARN_INTEG
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
fun KetoBrainSimulatorScreen(modifier: Modifier = Modifier) {
  var simMode by remember { mutableStateOf(SimulatorMode.GLUCOSE) }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(DeepDarkBlue)
  ) {
    LazyColumn(
      modifier = Modifier
        .fillMaxWidth()
        .weight(1f),
      contentPadding = PaddingValues(top = 12.dp, bottom = 24.dp),
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
    }
  }
}

@Composable
fun KetoBrainHandbookScreen(modifier: Modifier = Modifier) {
  var activeTab by remember { mutableStateOf(NavigationTab.BIOCHEMISTRY) }
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
        subtitle = "Zintegrowany ekosystem i rola mieliny",
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
          "Zdrowie błon mitochondrialnych opiera się na kardiolipinie. Spożywanie przetworzonych olejów roślinnych niszczy tę strukturę, powodując ucieczkę elektronów i załamanie Silnika M."
        ),
        shockingTakeaway = "SZOKUJĄCY FAKT: Opieranie metabolizmu mózgu wyłącznie na Silniku G (z powodu uszkodzenia mitochondriów) przypomina palenie w piecu stertą papierowych gazet – daje szybki ogień i masę popiołu, zamiast stabilnego ciepła dębowego drewna."
      ),
      DetailSection(
        id = "bio_4",
        title = "4. Przekleństwo Glukozy i Blokada Insuliny",
        subtitle = "Cerebral Glucose Hypometabolism",
        bulletPoints = listOf(
          "Aby neurony mogły skutecznie przetrawzać glukozę, niezbędna jest wysoka wrażliwość receptorów dla insuliny (IR) znajdujących się na synapsach i obrzeżach bariery krew-mózg.",
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
      ),
      DetailSection(
        id = "bio_6",
        title = "6. Porwanie Tryptofanu i Szlak Kynureninowy",
        subtitle = "Jak neurozapalenie kradnie serotoninę i melatoninę",
        bulletPoints = listOf(
          "W normalnych warunkach mózg konwertuje tryptofan w serotoninę (nastrój) oraz melatoninę (głęboki sen).",
          "Pod wpływem chronicznego zapalenia i wolnych rodników (wywołanych przez AGEs i kwas linolowy), tryptofan zostaje porwany ze szlaku serotoniny na neurotoksyczną odnogę szlaku kynureninowego (KYN).",
          "Skutek to drastyczny niedobór serotoniny i melatoniny, połączony z uderzeniowym (nawet 100-krotnym!) wzrostem produkcji neurotoksycznego kwasu glutaminowego (glutamate).",
          "Nadmiar glutaminianu dławi receptor NMDA (tzw. ekscytotoksyczność glutaminianu), powodując masową śmierć synaps, uszkodzenie mitochondriów i chroniczne lęki."
        ),
        shockingTakeaway = "SZOKUJĄCY FAKT: Stany lękowe i bezsenność w depresji to nie brak leków, lecz fizyczne porwanie tryptofanu pod wpływem diety prozapalnej (cukry i oleje nasienne)!"
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
        title = "3. Kryteria Bradforda Hilla: Co jest Nauka?",
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

  val dietsClinicalSections = remember {
    listOf(
      DetailSection(
        id = "clin_1",
        title = "1. ZABURZENIA LĘKOWE (Anxiety)",
        subtitle = "Jak ketony odbudowują spokój neuronów",
        bulletPoints = listOf(
          "Stany lękowe wynikają z nadreaktywności jądra migdałowatego (amygdala), które na chroniczne niedobory energii (ATP) odpowiada sygnałami zagrożenia.",
          "Ketony (BHB) stymulują ekspresję enzymu GAD, który gwałtownie konwertuje kwas glutaminowy (toksyczny neuroprzekaźnik pobudzający, lękotwórczy) do uspokajającego GABA.",
          "Dieta Keto oraz Carnivore usuwa ksenobiotyki roślinne i nadmiar glutenu, które niszczą spójność jelitową, zapobiegając uwalnianiu zapalnych lipopolisacharydów (LPS).",
          "Samo Paleo często dopuszcza ziarna i orzechy bogate w kwas fitynowy, przez co blokuje mineralizację, dlatego stany lękowe najlepiej wycisza ścisłe Quiet Keto lub Carnivore."
        ),
        shockingTakeaway = "WSKAZÓWKA DR EDE: Przy mocnych stanach lękowych nie bój się eliminacji warzyw. Quiet Carnivore w 2-3 tygodnie potrafi wygasić 90% lęków obwodowych!"
      ),
      DetailSection(
        id = "clin_2",
        title = "2. DEPRESJA I MGŁA MÓZGOWA (Depression & Fog)",
        subtitle = "Przeciwzapalna tarcza mitochondriów",
        bulletPoints = listOf(
          "Współczesna psychiatria uznaje depresję kliniczną za stan zapalny tkanki nerwowej (Neuroinflammation).",
          "Komórki glejowe obciążone cukrem generują niszczące cytokiny. Ketony blokują inflamasom NLRP3, wygaszając kaskadę zapalną w mózgu.",
          "Cholesterol i tłuszcze nasycone dostarczane w Carnivore i Keto ratują osłonki mielinowe oligodendrocytów, umożliwiając szybki transport sygnałów i likwidując apatię.",
          "Regularne zjazdy insulinowe po glukozie wywołują poranne wyrzuty kortyzolu (stresu), podsycając płaczliwość i brak napędu. Stała ketoza omija te wahania pętli hormonów."
        ),
        shockingTakeaway = "WSKAZÓWKA DR EDE: Depresja lekooporna rewelacyjnie odpowiada na dietę Keto opartą na czerwonym mięsie, jajach i zdrowych lipidach zwierzęcych."
      ),
      DetailSection(
        id = "clin_3",
        title = "3. CHOROBA DWUBIEGUNOWA (ChAD)",
        subtitle = "Stabilizacja ładunku błony neuronu",
        bulletPoints = listOf(
          "Choroba dwubiegunowa cechuje się niestabilnością poziomu elektrolitów (sodu i wapnia) po obu stronach błony neuronu, co sprzyja stanom manii i depresji.",
          "Większość stabilizatorów nastroju (lit, kwas walproinowy) działa właśnie na pompy sodowo-potasowe oraz kanały wapniowe.",
          "Ciała ketonowe działają naturalnie identycznie jak te leki: nasycając neurony ATP, umożliwiają bezwysiłkową pracę pomp sodowo-potasowych (Na+/K+-ATP-aza) i stabilizują elektrykę neuronów.",
          "Charakterystyczna dla ChAD dysfunkcja Kompleksu I w mitochondrialnym łańcuchu zostaje ominięta przez ketony bezpośrednio do Kompleksu II.",
          "Duża literatura naukowa (w tym pionierskie badania dr. Chrisa Palmera z Harvardu) opisuje spektakularne, trwałe remisje ChAD po wdrożeniu Quiet Keto."
        ),
        shockingTakeaway = "WSKAZÓWKA DR EDE: ChAD wymaga precyzyjnej i ścisłej kontroli ciał ketonowych na poziomie minimum 1.5 - 2.5 mmol/l we krwi."
      ),
      DetailSection(
        id = "clin_4",
        title = "4. ADHD I LABILNOŚĆ EMOCJONALNA",
        subtitle = "Dopływ paliwa do płatów czołowych",
        bulletPoints = listOf(
          "Płaty czołowe osób z ADHD wykazują obniżoną aktywność metaboliczną (hypometabolism), co upośledza funkcje wykonawcze i kontrolę impulsów.",
          "Jedzenie cukrów daje chwilowy wyrzut dopaminy, po czym następuje gwałtowny spadek glukozy i wyrzut adrenaliny, owocujący agresją, drażliwością i brakiem uwagi.",
          "Quiet Paleo (Krok 1) eliminuje sztuczne barwniki, konserwanty, cukier i nabiał, co drastycznie zmniejsza nadpobudliwość u 50-70% pacjentów.",
          "Quiet Keto lub Carnivore idą jeszcze dalej - zapewniają płatom czołowym stały, 24-godzinny poziom czystej energii i stabilizują dopaminowanie mózgu."
        ),
        shockingTakeaway = "WSKAZÓWKA DR EDE: Zanim podasz dziecku lub sobie silne leki stymulujące (jak ritalin), wypróbuj Quiet Paleo eliminując całkowicie cukier oraz gluten na 30 dni."
      ),
      DetailSection(
        id = "clin_5",
        title = "5. SCHIZOFRENIA I CYKRZYCA TYPU 3",
        subtitle = "Metaboliczne cofanie psychoz",
        bulletPoints = listOf(
          "Skrajne zaburzenia psychiczne, w tym schizofrenia, mocno korelują z insulinoopornością mózgową i niezdolnością neuronów do absorpcji glukozy.",
          "Dr Georgia Ede leczyła z pełnym sukcesem pacjentów ze schizofrenią oporną na leki, wykorzystując wyłącznie terapeutyczne, dobrze skomponowane diety ketogenne.",
          "Ominięcie zablokowanego receptorowo szlaku glukozy ratuje neurony przed śmiercią energetyczną i trwale uśmierza głosy oraz omamy.",
          "Dodatkowo, wyeliminowanie wszystkich antyodżywczych substancji z roślin zapobiega autoimmunologicznym atakom na neuroreceptory."
        ),
        shockingTakeaway = "WSKAZÓWKA DR EDE: Schizofrenia to patologia metaboliczna mózgu. Przywrócenie zasilania mitochondrialnego za pomocą ketonów wykazuje rewolucyjne działanie terapeutyczne."
      )
    )
  }

  val foodLibrarySections = remember {
    listOf(
      DetailSection(
        id = "food_1",
        title = "1. DOSKONAŁE SUPERPALIWA (Zezwolone)",
        subtitle = "Pokarmy o najwyższej gęstości odżywczej",
        bulletPoints = listOf(
          "Mięsa przeżuwaczy (Wołowina, jagnięcina, dziczyzna, kozina): Królowie ludzkiego stołu. Dostarczają 100% przyswajalnego żelaza hemowego, cynku, kreatyny, karnityny oraz unikalnej witaminy B12.",
          "Żółtka jajek: Bogactwo choliny (materiał budulcowy dla neuroprzekaźnika koncentracji i pamięci - acetylocholiny) oraz luteiny ocalającej siatkówkę.",
          "Masło Ghee (Klarowane): Czysty tłuszcz nasycony pozbawiony kazeiny i laktozy. Zawiera maślan regenerujący nabłonek jelitowy.",
          "Tłuste ryby dzikie (łosoś, makrela, szproty): Niezbędne źródła DHA (kwas dokozaheksaenowy), budującego 20% kory mózgowej i zapobiegającego demencji.",
          "Awokado i oliwa z oliwek (extra virgin): Jedyne owoce/rośliny o ujemnej toksyczności, dostarczające stabilnych jednonienasyconych tłuszczów."
        ),
        shockingTakeaway = "SUGESTIA METABOLICZNA: Człowiek potrafi przeżyć całe życie jedząc wyłącznie wołowinę, sól i pijąc wodę. Nasz mózg ewoluował na tłuszczach zwierzęcych!"
      ),
      DetailSection(
        id = "food_2",
        title = "2. UKRYTE TOKSYNY ROŚLINNE (Unikaj lub Eliminuj)",
        subtitle = "Naturalna broń biologiczna flory",
        bulletPoints = listOf(
          "Szczawiany (Oxalates - szpinak, buraki, migdały): Tworzą igiełkowate kryształy szczawianu wapnia. Odkładają się w stawach, nerkach i tkance mózgoworuchowej stymulując mikroglej.",
          "Lektyny (Lectins - gluten, fasole, orzechy, psiankowate): Aglutyniny rozrywające barierę jelitową (Leaky Gut), ułatwiając toksynom wniknięcie do osocza krwi.",
          "Salicylany (Salicylates - zioła, przyprawy, owoce): Bardzo wiele osób ma silną nadwrażliwość psychiczną na salicylany. Wyzwala to lęki, nocne poty, bezsenność i nadpobudliwość u dzieci.",
          "Kwas fitynowy (Phytates - zboża, orzechy, soja): Blokuje wchłanianie krytycznych dla mózgu minerałów: cynku, magnezu, żelaza i wapnia."
        ),
        shockingTakeaway = "SUGESTIA METABOLICZNA: Rośliny nie mogą uciekać przed drapieżnikiem. Ich jedyną obroną są wyrafunowane toksyny chemiczne celowane w układ pokarmowy i nerwy roślinożerców!"
      ),
      DetailSection(
        id = "food_3",
        title = "3. RAFINOWANE OLEJE ROŚLINNE I CUKIER",
        subtitle = "Mechanizm niszczenia mitochondriów",
        bulletPoints = listOf(
          "Oleje przemysłowe (rzepakowy, sojowy, słonecznikowy, kukurydziany) to nowość technologiczna. Powstają w drodze skomplikowanej rafinacji w 13 krokach za pomocą heksanu (petrochemicznego rozpuszczalnika), ługowania sodem i odwonienia parowego (powyżej 180°C), co zmienia wolne tłuszcze w truciznę.",
          "Wspomniane oleje zawierają drastyczne ilości kwasu linolowego (Omega-6). Organizm człowieka potrzebuje aż 680 dni celowego unikania, by pozbyć się zaledwie połowy zmagazynowanego kwasu linolowego z tkanki tłuszczowej!",
          "Wysokie spożycie margaryn i olejów nasiennych niszczy kardiolipinę – fosfolipid budujący wewnętrzną błonę mitochondriów, co wywołuje załamanie ETC i potężne przecieki wolnych rodników (ROS).",
          "Z kolei cukier i fruktoza (oraz słodziki wywołujące neurotoksyczną nadwrażliwość NMDA) stale stymulują insulinę, potęgując obrzęki i proces glikacji neuronów."
        ),
        shockingTakeaway = "SZOKUJĄCY FAKT: Mózg i glej próbują spalać kwas linolowy jako awaryjne paliwo tłuszczowe, co drastycznie wzmaga stres oksydacyjny i uwalnia niszczycielskie produkty rozpadu (OXLAMs)."
      ),
      DetailSection(
        id = "food_4",
        title = "4. UPADEK MITÓW: Blueberries, Czekolada i Czerwone Wino",
        subtitle = "Szokująca prawda o tzw. Antyoksydantach",
        bulletPoints = listOf(
          "Wielkie badania kliniczne (RCT) dowiodły, że suplementowanie rzekomych antyoksydantów (witaminy C i E, beta-karoten) nie tylko nie pomogło chorym, lecz zwiększyło rany rakowe i śmiertelność (beta-karoten u palaczy).",
          "Słynne borówki (blueberries) mają znikomą przyswajalność polifenoli (anthocyanins). Truskawka (strawberry) deklasuje je pod każdym względem, zawierając 7 razy więcej witaminy C i tylko połowę cukru borówki!",
          "Czerwone wino jako eliksir zdrowia to mit epidemiologiczny. Aby uzyskać terapeutyczną dawkę resweratrolu (polifenolu, będącego u roślin grzybobójczym pestycydem!), należałoby pić aż 500 butelek wina dziennie.",
          "Brokułowy 'cud' czyli sulforafan to w rzeczywistości toksyczny insektycyd ('bomba musztardowa' chroniąca roślinę). Nie jest pokarmem, lecz ksenobiotykiem, który wątroba próbuje jak najszybciej wydalić z komórek."
        ),
        shockingTakeaway = "SZOKUJĄCY FAKT FIZJOLOGICZNY: W 2012 roku Departament Rolnictwa USA (USDA) oficjalnie wycofał i usunął internetową bazę danych ORAC, przyznając publicznie, że testy antyoksydacyjne w próbówce (in vitro) nie wykazują żadnego przełożenia na zdrowie żywego człowieka!"
      ),
      DetailSection(
        id = "food_5",
        title = "5. ROŻNICE MIĘDZY DIETAMI (Porównanie)",
        subtitle = "Droga eliminacyjna: Paleo ➔ Keto ➔ Carnivore",
        bulletPoints = listOf(
          "Quiet Paleo (Krok 1): Dieta naturalna, bez zbóż, bez nabiału, bez cukru opartego na rafinacji. Dopuszcza warzywa, owoce i chude mięsa. Działa doskonale jako metaboliczny punkt wyjścia dla każdego.",
          "Quiet Keto (Krok 2): Dieta wysokotłuszczowa, niskowęglowodanowa (do 20g-30g netto). Wycina owoce sadowe i sterylne skrobie. Wprowadza mózg w stałą ketozę, wygaszając lęki obwodowe.",
          "Quiet Carnivore (Krok 3): Absolutne plant-free i egg/dairy-free. Najwyższy i najkrótszy stopień eliminacyjny dający ulgę w najgłębszych depresjach, schizofrenii i psychozach."
        ),
        shockingTakeaway = "REGUŁA DR EDE: Uzdrowienie mózgu to nie mądrość 'dodawania' cudownych owoców czy suplementów, lecz mądrość odejmowania i bezwzględnej SUBTRAKCJI niszczących substancji metabolicznych!"
      )
    )
  }

  val medWarnSections = remember {
    listOf(
      DetailSection(
        id = "warn_1",
        title = "1. OSTRZEŻENIE O LEKACH (⚠️ Przeczytaj Koniecznie)",
        subtitle = "Hipotensja, leki psychiatryczne i rola lekarza",
        bulletPoints = listOf(
          "Gdy wchodzisz w ketozę, poziom insuliny spada, co powoduje intensywne usuwanie wody i sodu przez nerki (tzw. natriureza).",
          "Osoby przyjmujące leki na nadciśnienie (np. betablokery, inhibitory ACE) mogą doświadczyć gwałtownych, groźnych dla zdrowia spadków ciśnienia (hipotensja). Dawkowanie tych leków musi być niezwłocznie dostosowane!",
          "Mózg w ketozie staje się znacznie bardziej wrażliwy na leki neuroaktywne. Leki psychiatryczne (lit, depakine, benzodiazepiny, neuroleptyki) zaczynają działać znacznie silniej.",
          "Poprawa wydolności mitochondriów sprawia, że dotychczasowa dawka leku może stać się toksyczna. Monitorowanie stężenia leków (szczególnie Litu) we krwi jest bezwzględnym nakazem!"
        ),
        shockingTakeaway = "⚠️ UWAGA: Dr Georgia Ede kładzie ogromny nacisk na to, by zmiana diety przy lekach psychotropowych odbywała się zawsze po konsultacji z lekarzem prowadzącym. Nigdy nie odstawiaj leków samowolnie!"
      ),
      DetailSection(
        id = "warn_2",
        title = "2. KETO GRYPA (Keto Flu) - Jak Uniknąć?",
        subtitle = "Elektrolitowy spadek adaptacyjny",
        bulletPoints = listOf(
          "Keto grypa to zestaw przejściowych objawów trwających od 3 do 10 dni, wynikających ze spadku elektrolitów i zmiany enzymów szlaków energetycznych.",
          "Objawy: silny ból głowy, osłabienie, skurcze mięśni, drżenie rąk, lęk, zaparcia/biegunki.",
          "Rozwiązanie: Pij 1.5 - 2 litry osolonej wody dziennie (szczypta soli kłodawskiej / morskiej na szklankę).",
          "Pij ciepły rosół ugotowany na kościach szpikowych (bone broth) bogaty w glicynę i minerały.",
          "Suplementuj cytrynian magnezu (350-450mg magnezu przed snem) oraz uzupełniaj potas z dozwolonych źródeł."
        ),
        shockingTakeaway = "Rozwiązanie tkwi w soli! Większość objawów keto grypy to po prostu odwodnienie i spadek sodu we krwi, które można odwrócić w 15 minut szklanką słonej wody."
      ),
      DetailSection(
        id = "warn_3",
        title = "3. HARMONOGRAM ADAPTACJI (Timeline)",
        subtitle = "Czego spodziewać się dzień po dniu i tydzień po tygodniu",
        bulletPoints = listOf(
          "Tydzień 1 (Dni 1-7): Wypłukiwanie glikogenu z wątroby i mięśni. Szybki spadek wagi (głównie woda). Możliwe objawy keto-flu. Ustabilizowanie pierwszych skoków glukozowych.",
          "Tydzień 2-4: Wątroba produkuje coraz więcej BHB. Mózg uczy się korzystać z ketonów. Wyraźny zanik porannych lęków i wieczornych napadów głodu.",
          "Miesiąc 2-3: Pełna ketoadaptacja komórkowa. Dochodzi do mitochondrialnej biogenezy (tworzenie nowych, sprawnych elektrowni w komórkach). Trwała eliminacja mgły mózgowej i pełny spokój nastroju."
        ),
        shockingTakeaway = "Daj sobie czas: Adaptacja mózgu do optymalnego spalania tłuszczów to maraton, a nie sprint. Prawdziwe cuda psychiczne zaczynają się wokół 8-12 tygodnia!"
      ),
      DetailSection(
        id = "warn_4",
        title = "4. KROKI ELIMINACJI WEDŁUG DR. EDE",
        subtitle = "Quiet Paleo -> Quiet Keto -> Quiet Carnivore",
        bulletPoints = listOf(
          "Krok 1 (Quiet Paleo): Eliminacja zbóż, glutenu, nabiału pasteryzowanego i cukru. Skupienie na świeżym mięsie i bezpiecznych roślinach.",
          "Krok 2 (Quiet Keto): Przejście w stan stabilnej ketozy. Drastyczne ograniczenie węglowodanów, odcięcie owoców sadowych i warzyw o wysokiej zawartości toksyn obronnych.",
          "Krok 3 (Quiet Carnivore): Najwyższa forma terapii eliminacyjnej. Wyklucza wszystkie rośliny bez wyjątku. Dieta mięso-sól-woda, ratująca pacjentów w najcięższych psychozach i depresjach."
        ),
        shockingTakeaway = "Zasada Dr Ede: Zacznij od Kroku 1 lub 2. Jeśli objawy psychiczne nie ustąpią w 100%, wejdź na Krok 3 na okres 30 dni, a potem powoli reintrodukuj składniki."
      )
    )
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(DeepDarkBlue)
  ) {
    // Custom Horizontally Scrollable Selector
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .horizontalScroll(rememberScrollState())
        .padding(horizontal = 16.dp, vertical = 12.dp)
        .testTag("tab_container"),
      horizontalArrangement = Arrangement.spacedBy(8.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      val tabs = listOf(
        Triple(NavigationTab.BIOCHEMISTRY, "🧬 Biochemia", "tab_biochemistry"),
        Triple(NavigationTab.METHODOLOGY, "🔬 Krytyka Nauki", "tab_methodology"),
        Triple(NavigationTab.DIETS_CLINICAL, "🥩 Diety i Wskazania", "tab_diets_clinical"),
        Triple(NavigationTab.FOOD_LIBRARY, "🛒 Produkty i Toksyny", "tab_food_library"),
        Triple(NavigationTab.MED_WARN_INTEG, "📋 Wdrażanie i Leki", "tab_med_warn_integ")
      )

      tabs.forEach { (tab, label, testTag) ->
        val isSelected = activeTab == tab
        Box(
          contentAlignment = Alignment.Center,
          modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (isSelected) EnergyAmber else DarkCardBg)
            .border(
              BorderStroke(1.dp, if (isSelected) EnergyAmber else DarkCardBorder),
              RoundedCornerShape(20.dp)
            )
            .clickable { activeTab = tab }
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .testTag(testTag)
        ) {
          Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = if (isSelected) Color.White else CleanWhite
          )
        }
      }
    }

    val currentList = when (activeTab) {
      NavigationTab.BIOCHEMISTRY -> biochemistrySections
      NavigationTab.METHODOLOGY -> methodologySections
      NavigationTab.DIETS_CLINICAL -> dietsClinicalSections
      NavigationTab.FOOD_LIBRARY -> foodLibrarySections
      NavigationTab.MED_WARN_INTEG -> medWarnSections
    }

    LazyColumn(
      modifier = Modifier
        .fillMaxWidth()
        .weight(1f),
      contentPadding = PaddingValues(top = 12.dp, bottom = 24.dp),
      verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      item {
        Text(
          text = when (activeTab) {
            NavigationTab.BIOCHEMISTRY -> "🧬 DOKŁADNE ZAGADNIENIA BIOLOGICZNE:"
            NavigationTab.METHODOLOGY -> "🔬 ABSURDY WSPÓŁCZESNEJ METODOLOGII:"
            NavigationTab.DIETS_CLINICAL -> "🥩 KLINICZNE WSKAZANIA DIETETYCZNE:"
            NavigationTab.FOOD_LIBRARY -> "🛒 DOBRE PRODUKTY I ANTY-ODŻYWIACZE:"
            NavigationTab.MED_WARN_INTEG -> "⚠️ KLUCZOWE WSKAZÓWKI I OSTRZEŻENIA:"
          },
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
      trackColor = Color(0xFF1E293B),
      modifier = Modifier
        .fillMaxWidth()
        .height(6.dp)
        .clip(RoundedCornerShape(3.dp))
    )
  }
}

data class ProtocolDetail(
  val name: String,
  val description: String,
  val clinicalTarget: String,
  val goldenRules: List<String>,
  val allowedCategories: Map<String, List<String>>,
  val forbiddenFoods: List<String>,
  val mealPlan: List<Pair<String, String>>,
  val physiologicalFact: String
)

@Composable
fun TrackScreen() {
  var selectedProtocol by remember { mutableStateOf("keto") }
  var selectedTransition by remember { mutableStateOf(0) }

  // Interactive Checklist State to support user compliance tracking
  var habitsChecklist by remember {
    mutableStateOf(
      mapOf(
        "sol_woda" to false,
        "tluste_proteiny" to false,
        "zero_olejow" to false,
        "zero_cukru" to false,
        "sen_regeneracja" to false
      )
    )
  }

  val protocols = remember {
    mapOf(
      "paleo" to ProtocolDetail(
        name = "Quiet Paleo (Krok 1)",
        description = "Etap przygotowawczy wykluczający sztuczne, przetworzone pożywienie, cukry proste, nabiał pasteryzowany oraz toksyczne oleje nasienne. To reset układu pokarmowego i eliminacja lektyn zbożowych bez wprowadzania organizmu w stan głębokiej ketozy.",
        clinicalTarget = "ZALECENIA KLINICZNE: Idealny dla osób z łagodną mgłą mózgową, bólami głowy, kłopotami trawiennymi i początkiem spadku koncentracji. Służy jako bezpieczny punkt startowy (reboot metaboliczny).",
        goldenRules = listOf(
          "Wyeliminuj 100% zbóż (w tym gluten) i kukurydzy",
          "Całkowity zakaz tłuszczów trans i rafinowanych olejów roślinnych (rzepak, słonecznik)",
          "Wyrzuć cukier rafinowany i sztuczne słodziki słabej jakości",
          "Wybieraj tylko jaja z wolnego wybiegu (klasa 0) i wysokiej jakości mięso",
          "Ogranicz owoce do jagodowych i unikaj warzyw psiankowatych w nadmiarze"
        ),
        allowedCategories = mapOf(
          "🥩 Mięsa z Pewnego Źródła" to listOf(
            "Czysta wołowina (łopatka, rostbef, antrykot)",
            "Dziczyzna leśna (sarnina, dzik, jeleń)",
            "Zagrodowy kurczak i wolnowybiegowy indyk",
            "Mięso z kaczki, gęsi lub przepiórki ze skórą"
          ),
          "🐟 Ryby z Dzikich Łowów" to listOf(
            "Dziki łosoś sockeye (źródło czystych kwasów omega-3)",
            "Świeży pstrąg potokowy z czystych rzek",
            "Morska makrela pacyficzna",
            "Dzikie sardynki w czystej oliwie z oliwek"
          ),
          "🍳 Najlepsze Tłuszcze Klasy Premium" to listOf(
            "Żółtka ekologicznych jaj (klasa 0)",
            "Oliwa extra virgin tłoczona na zimno (bogata w polifenole)",
            "Czysty olej z awokado (niepodgrzewany do wysokich temperatur)",
            "Olej kokosowy tłoczony na zimno (dziewiczy)"
          ),
          "🥦 Bezpieczne Warzywa & Owoce o Niskiej Toksyczności" to listOf(
            "Gotowane słodkie ziemniaki (bataty - bez skóry)",
            "Gotowana marchew, dynia hokkaido (łatwo przyswajalna skrobia)",
            "Świeże borówki, maliny, jagody leśne (bomba antyoksydacyjna)",
            "Szparagi oraz sałata rzymska (łagodne dla jelit)"
          )
        ),
        forbiddenFoods = listOf(
          "Wszelkie pieczywo i wypieki (chleb pszenny, żytni, tostowy, drożdżówki)",
          "Zwykły biały ziemniak (zawiera silne, drażniące lektyny i solaninę)",
          "Pasteryzowane mleko krowie, sery przemysłowe i jogurty słodzone",
          "Oleje przemysłowe (rzepakowy, słonecznikowy, sojowy, kukurydziany, margaryny)",
          "Nasiona roślin strączkowych: fasola, soja, groch i kwas fitynowy z orzechów"
        ),
        mealPlan = listOf(
          "Śniadanie 🌄" to "Jajecznica z 3-4 ekologicznych jaj smażona na gęsim smalcu z dodatkiem drobno pokrojonej pieczonej dziczyzny. Do tego połówka dojrzałego awokado obficie posypana czarnym sezamem i solą kłodawską oraz miseczka świeżych borówek leśnych, które dostarczają polifenoli zapobiegających starzeniu mikrogla.",
          "Obiad ☀️" to "Wielka pieczona pierś z dzikiej kaczki podana z aksamitnym purée z batatów (gotowanych z dodatkiem łyżki oleju kokosowego tłoczonego na zimno). Jako dodatek lekkostrawny: szparagi pieczone w piecu z odrobiną czosnku i gruboziarnistej soli himalajskiej.",
          "Kolacja 🌌" to "Grillowany filet z dzikiego pstrąga potokowego skropiony świeżym sokiem z cytryny i oliwą z oliwek extra virgin. Podawany ze świeżą sałatą rzymską, obranym ze skórki i pozbawionym toksycznych gniazd nasiennych ogórkiem oraz garścią czarnych oliwek drylowanych."
        ),
        physiologicalFact = "SZOKUJĄCY FAKT FIZJOLOGICZNY: Gluten jest budową strukturalną zbliżony do niektórych białek w mózgu. Przez nieszczelną barierę jelitową (Leaky Gut) przeciwciała antyglutenowe potrafią bezpośrednio atakować komórki móżdżku, wywołując tzw. glutenową ataksję i głębokie, przewlekłe lęki bez uchwytnej przyczyny."
      ),
      "keto" to ProtocolDetail(
        name = "Quiet Keto (Krok 2)",
        description = "Paliwo ratunkowe dla wygłodzonych neuronów. Poprzez redukcję węglowodanów do minimum, wątroba przekształca tłuszcze w ciała ketonowe (BHB), które z łatwością przekraczają barierę krew-mózg bez udziału insuliny. Omija to uszkodzony szlak glikolizy neuronów.",
        clinicalTarget = "ZALECENIA KLINICZNE: Kluczowa pomoc przy depresji, stanach lękowych, chorobie dwubiegunowej (ChAD), insulinooporności mózgowej i chronicznym zmęczeniu psychicznym. Generuje nieskazitelną, stabilną sygnaturę energetyczną.",
        goldenRules = listOf(
          "Ogranicz węglowodany netto do maksymalnie 20-30g dziennie",
          "Skoncentruj się na tłuszczach nasyconych i jednonienasyconych zwierzęcych",
          "Wprowadź czysty kwas kaprylowy (olej MCT C8) dla błyskawicznej ketogenezy w wątrobie",
          "Uzupełniaj sód, potas i magnez z uwagi na zwiększoną natriurezę nerkową w ketozie",
          "Wyeliminuj nabiał krowi, jeśli po jego spożyciu czujesz mgłę mózgową"
        ),
        allowedCategories = mapOf(
          "🥩 Tłuste Mięsa & Podroby" to listOf(
            "Marmurkowa wołowina (antrykot, zrazy, karkówka wołowa)",
            "Wieprzowina o wysokiej zawartości tłuszczu (boczek tradycyjny bez chemii)",
            "Tłuste żeberka, pieczona kaczka, gęś ze skórą",
            "Wątróbki wołowe, serca i ozory (najbogatsze naturalne źródła makroelementów)"
          ),
          "🐟 Czyste Tłuste Ryby Oceaniczne" to listOf(
            "Tłusty dziki łosoś sockeye (połów z zimnych wód)",
            "Wątroba dorsza we własnym tłuszczu (bomba witamin A i D3)",
            "Morska tłusta makrela wędzona lub pieczona",
            "Szproty wędzone w oleju kokosowym"
          ),
          "🧈 Najzdrowsze Lipidy Ketogeniczne" to listOf(
            "Masło klarowane ghee o zerowej zawartości laktozy i kazeiny",
            "Łój wołowy lub domowy smalec wieprzowy/gęsi",
            "Olej MCT C8 (czysty kwas kaprylowy - ultra szybka energia dla neuronów)",
            "Awokado tłoczone zimno lub oliwa z oliwek najwyższej jakości"
          ),
          "🥑 Dozwolone Zielone Low-Carb" to listOf(
            "Całe czyste awokado (doskonała podaż potasu i magnezu)",
            "Ogórek świeży obrany bez toksycznych gniazd nasiennych",
            "Ograniczone ilości młodego szpinaku (blanszowanego gorącą wodą)",
            "Liście świeżej sałaty rzymskiej jako baza miski tłuszczowej"
          )
        ),
        forbiddenFoods = listOf(
          "Wszystkie zboża, mąki, makarony, ryż, pieczywo bezglutenowe",
          "Warzywa korzeniowe o dużej ilości skrobi (marchew gotowana, buraki, bataty, ziemniaki)",
          "Owoce sadowe i słodkie (jabłka, gruszki, banany, winogrona, czereśnie)",
          "Oleje przemysłowe (rzepakowy, słonecznikowy, sojowy, kukurydziany i margaryny kuchenne)",
          "Słodziki chemiczne i aspartam w nadmiarze (drastycznie zaburzają mikrobiotę jelitową)"
        ),
        mealPlan = listOf(
          "Śniadanie 🌄" to "Puszysty, złocisty omlet przygotowany wyłącznie z 4 samych żółtek i 1 całego jajka, smażony wolno na 2 dużych łyżkach prawdziwego masła ghee, przełożony chrupiącą porcją ekologicznego boczku bez dodatków chemicznych. Do picia: Kawa kuloodporna (Bulletproof Coffee) z dodatkiem 15ml czystego oleju MCT C8 oraz łyżką masła klarowanego, co drastycznie podbija stężenie acetylocholiny w mózgu.",
          "Obiad ☀️" to "Mielona łopatka wołowa uformowana w grube kotlety i upieczona w piecu, nadziewana w środku prawdziwym chłodnym masłem czosnkowym. Jako dodatek: połówka dużego awokado oprószona krystaliczną solą z kopalni Kłodawa i świeżym szczypiorkiem.",
          "Kolacja 🌌" to "Wykwintny tatar ze świeżo skrobanej polędwicy wołowej, obficie zalany oliwą z oliwek extra virgin najwyższej jakości, podany z dwoma surowymi żółtkami jaj eko, odrobiną puszystego masła i cebulki dymki dla smaku."
        ),
        physiologicalFact = "SZOKUJĄCY FAKT FIZJOLOGICZNY: Mózg osoby z depresją lub chorobą Alzheimera jest w stanie patologicznej insulinoporności (tzw. cukrzyca typu 3). Neurony dosłownie umierają z głodu obok wysokiego stanu glukozy we krwi, ponieważ nie potrafią jej spalić. Ciała ketonowe (BHB) przenikają barierę krew-mózg bez udziału insuliny, ratując komórki przed śmiercią energetyczną i neurodegeneracją!"
      ),
      "carnivore" to ProtocolDetail(
        name = "Quiet Carnivore (Krok 3)",
        description = "Absolutne i bezkompromisowe ultimatum dla układu immunologicznego i nerwowego. Wyklucza 100% roślinnych antynutrientów, szczawianów niszczących stawy i nerki, salicylanów pobudzających oraz błonnika, który drażni zniszczone nieszczelne jelita.",
        clinicalTarget = "ZALECENIA KLINICZNE: Ekstremalne wyciszenie układu immunologicznego. Zastosuj w ciężkich depresjach lekoopornych, psychozach, schizofrenii, autyzmie u dzieci oraz przewlekłych lękach panicznych z agorafobią.",
        goldenRules = listOf(
          "Jedz wyłącznie produkty pochodzenia odzwierzęcego",
          "Nie spożywaj żadnych warzyw, owoców, przypraw roślinnych ani pieprzu (anty-nutrienty)",
          "Pij obficie sól kłodawską lub morską celtycką z porządną wodą mineralną bogatą w magnez",
          "Unikaj białka jaj - spożywaj wyłącznie żółtka (białko silnie gasi enzymy trawienne i może uczulać)",
          "Bazuje głównie na czerwonym mięsie przeżuwaczy (wołowina, jagnięcina, baranina)"
        ),
        allowedCategories = mapOf(
          "🥩 Czerwień Mięsa Przeżuwaczy (Podstawa)" to listOf(
            "Tłuste steki z wołowego antrykotu (steaki Ribeye klasy premium)",
            "Żeberka i tłusty szponder wołowy do pieczenia",
            "Marmurkowa jagnięcina, gulasz barani i koźlina",
            "Dziczyzna leśna o wyższej, naturalnej zawartości tłuszczów nasyconych"
          ),
          "🍳 Żółtka & Masła Klarowane" to listOf(
            "Żółtka ekologicznych jajek (klasa 0 - bez immunogennych białek)",
            "Prawdziwe domowe masło klarowane ghee bez śladów białka mleka",
            "Czysty łój wołowy (najlepsze źródło kwasu stearynowego dla mitochondriów)",
            "Smalec gęsi tłoczony tradycyjnie na zimno"
          ),
          "🧬 Podroby o Gigantycznej Gęstości Odżywczej" to listOf(
            "Wątroba wołowa (prawdziwa ewolucyjna multiwitamina biologiczna)",
            "Szpik kostny z kości wołowych pieczony piecowo",
            "Nerki cielęce i ozory wołowe (niezrównane bogactwo żelaza heme)",
            "Kremowo-delikatne serce wołowe bogate w koenzym Q10"
          ),
          "🧂 Niezbędne Minerały i Płyny Elektrolitowe" to listOf(
            "Sól kłodawska kamienna nieoczyszczona (sód z minerałami towarzyszącymi)",
            "Sól celtycka szara bogata w drogocenne pierwiastki śladowe",
            "Czysta woda głębinowa niegazowana o wysokiej mineralizacji",
            "Prawdziwy, długo warzony rosół na kościach szpikowych (bomba kolagenowa)"
          )
        ),
        forbiddenFoods = listOf(
          "Wszystkie warzywa, w tym sałata rzymska, szpinak, awokado (ponieważ zawierają substancje obronne roślin)",
          "Wszystkie owoce, jagody leśne, orzechy, nasiona",
          "Wszelkie przyprawy ziołowe, pieprz czarny, pieprz cayenne (substancje drażniące)",
          "Kawa, herbata zielona, czarna, herbatki ziołowe (główne źródła rażących szczawianów)",
          "Białka jaj, nabiał krowi płynny, sery (mogą wywoływać silną kaskadę zapalną)"
        ),
        mealPlan = listOf(
          "Śniadanie 🌄" to "Śniadanie Wojownika: Królewski stek z antrykotu wołowego (Ribeye, ok. 350-400g) z pięknym marmurkowatym tłuszczem, posypany wyłącznie gruboziarnistą solą kłodawską, usmażony na krwisto/średnio na stopionej porcji tłuszczu wołowego (łoju). Do popicia szklanka letniej wody mineralnej z plasterkiem lodu i szczyptą soli sodowej.",
          "Obiad ☀️" to "Pieczone w piekarniku kości szpikowe wołowe (3 duże sztuki), podawane na gorąco z chrupiącą solą morską. Szpik wyjada się łyżeczką – to najbogatsze ewolucyjnie, nieskażone źródło kwasów DHA oraz fosfolipidów budujących osłonki mielinowe ludzkiego mózgu.",
          "Kolacja 🌌" to "Smakowita duszona wątróbka cielęca przygotowana na obfitym maśle ghee z dodatkiem plasterków pieczonego boczku krojonego grubo, uwieńczona surowym żółtkiem jaja kurzego dodanym tuż przed podaniem, by nie ścinać cennych enzymów."
        ),
        physiologicalFact = "SZOKUJĄCY FAKT FIZJOLOGICZNY: Dieta Carnivore jest jedyną dietą na świecie, która wyklucza 100% obronnych toksyn roślinnych. Rośliny nie mogą uciec przed zjedzeniem, przez co wyewoluowały złożoną broń chemiczną likwidującą ich wrogów. Wyłączenie tych neurotoksyn potrafi cofnąć psychozy schizofreniczne i napadowe lęki wegetatywne w 3-4 tygodnie!"
      )
    )
  }

  val activeProtocol = protocols[selectedProtocol] ?: protocols["keto"]!!

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
          text = "Poznaj kliniczne poziomy eliminacji pokarmowej z książki 'Change Your Diet, Change Your Mind'. Każdy stopień wyklucza więcej antynutrientów, dając ulgę neuronom.",
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
              Text("KROK 3: Maksymalna eliminacja (Dla stanów lękowych i ChAD)", fontSize = 11.sp, color = SoftGray)
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
            text = activeProtocol.description,
            fontSize = 13.sp,
            color = CleanWhite,
            lineHeight = 18.sp
          )
          Spacer(modifier = Modifier.height(12.dp))
          Text(
            text = activeProtocol.clinicalTarget,
            fontSize = 12.sp,
            color = EnergyAmber,
            fontWeight = FontWeight.Bold,
            lineHeight = 16.sp
          )
        }
      }
    }

    // Active Golden Rules
    item {
      CustomBorderCard(
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        borderColor = DarkCardBorder,
        borderWidth = 1.dp
      ) {
        Column(modifier = Modifier.padding(20.dp)) {
          Text(
            text = "👑 ZŁOTE ZASADY PROTOKOŁU",
            fontSize = 11.sp,
            fontWeight = FontWeight.ExtraBold,
            color = SoftGray,
            letterSpacing = 0.5.sp
          )
          Spacer(modifier = Modifier.height(10.dp))
          activeProtocol.goldenRules.forEach { rule ->
            Row(
              verticalAlignment = Alignment.Top,
              modifier = Modifier.padding(vertical = 4.dp)
            ) {
              Text("⭐", fontSize = 12.sp, modifier = Modifier.padding(end = 8.dp, top = 2.dp))
              Text(text = rule, fontSize = 12.sp, color = CleanWhite, lineHeight = 16.sp)
            }
          }
        }
      }
    }

    // Interactive Compliance Checklist
    item {
      CustomBorderCard(
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        borderColor = EnergyAmber,
        borderWidth = 1.dp,
        modifier = Modifier.testTag("habits_checklist_card")
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Text(
            text = "📋 CODZIENNY DZIENNIK REALIZACJI I SAMOPOCZUCIA",
            fontSize = 11.sp,
            fontWeight = FontWeight.ExtraBold,
            color = EnergyAmber,
            letterSpacing = 0.5.sp
          )
          Text(
            text = "Zweryfikuj dzisiejszy poziom zaangażowania protokołem. Zaznacz zrealizowane cele:",
            fontSize = 11.sp,
            color = SoftGray,
            modifier = Modifier.padding(vertical = 4.dp)
          )
          Spacer(modifier = Modifier.height(10.dp))

          val checklistItems = listOf(
            "sol_woda" to "💧 Wypito osoloną wodę lub rosół szpikowy (minimum 1.5l)",
            "tluste_proteiny" to "🥩 Główne posiłki oparte o czyste i tłuste proteiny zwierzęce",
            "zero_olejow" to "🚫 Całkowite unikanie olejów roślinnych i tłuszczów trans",
            "zero_cukru" to "🚫 Zero cukru, zbóż i pasteryzowanego nabiału",
            "sen_regeneracja" to "💤 Wygaszenie ekranów po 21:00 i dbałość o czysty sen"
          )

          checklistItems.forEach { (key, label) ->
            val isChecked = habitsChecklist[key] ?: false
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier
                .fillMaxWidth()
                .clickable {
                  val updated = habitsChecklist.toMutableMap()
                  updated[key] = !isChecked
                  habitsChecklist = updated
                }
                .padding(vertical = 6.dp)
            ) {
              Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                  .size(20.dp)
                  .clip(RoundedCornerShape(4.dp))
                  .background(if (isChecked) EnergyAmber else Color(0xFF1E293B))
                  .border(BorderStroke(1.dp, if (isChecked) EnergyAmber else Color(0xFF475569)), RoundedCornerShape(4.dp))
              ) {
                if (isChecked) {
                  Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Zaznaczone",
                    tint = Color.White,
                    modifier = Modifier.size(14.dp)
                  )
                }
              }
              Spacer(modifier = Modifier.width(10.dp))
              Text(
                text = label,
                fontSize = 12.sp,
                fontWeight = if (isChecked) FontWeight.Bold else FontWeight.Normal,
                color = if (isChecked) CleanWhite else SoftGray
              )
            }
          }

          val completedCount = habitsChecklist.values.count { it }
          if (completedCount == 5) {
            Spacer(modifier = Modifier.height(10.dp))
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(KetoneCyan.copy(0.12f))
                .border(BorderStroke(1.dp, KetoneCyan.copy(0.2f)), RoundedCornerShape(12.dp))
                .padding(12.dp)
            ) {
              Text(
                text = "🎉 FANTASTYCZNY DZIEŃ METABOLICZNY! Twoje neurony i osłonki mielinowe są pod pełną obroną!",
                fontSize = 12.sp,
                color = KetoneCyan,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
              )
            }
          }
        }
      }
    }

    // Detailed Allowed Foods Categories
    item {
      CustomBorderCard(
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        borderColor = DarkCardBorder,
        borderWidth = 1.dp
      ) {
        Column(modifier = Modifier.padding(20.dp)) {
          Text(
            text = "🛒 SZCZEGÓŁOWA LISTA DOZWOLONYCH PRODUKTÓW",
            fontSize = 11.sp,
            fontWeight = FontWeight.ExtraBold,
            color = SoftGray,
            letterSpacing = 0.5.sp
          )
          Spacer(modifier = Modifier.height(14.dp))
          activeProtocol.allowedCategories.forEach { (categoryName, foodList) ->
            Text(
              text = categoryName,
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              color = EnergyAmber,
              modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
            )
            foodList.forEach { food ->
              Row(
                verticalAlignment = Alignment.Top,
                modifier = Modifier.padding(vertical = 3.dp, horizontal = 4.dp)
              ) {
                Box(
                  modifier = Modifier
                    .padding(top = 5.dp)
                    .size(6.dp)
                    .clip(RoundedCornerShape(50))
                    .background(KetoneCyan)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = food, fontSize = 12.sp, color = CleanWhite, lineHeight = 16.sp)
              }
            }
            Spacer(modifier = Modifier.height(10.dp))
          }
        }
      }
    }

    // Forbidden Foods List
    item {
      CustomBorderCard(
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        borderColor = DarkCardBorder,
        borderWidth = 1.dp
      ) {
        Column(modifier = Modifier.padding(20.dp)) {
          Text(
            text = "🚫 BEZWZGLĘDNIE WYKLUCZONE ELEMENTY (Silne Toksyny)",
            fontSize = 11.sp,
            fontWeight = FontWeight.ExtraBold,
            color = SoftGray,
            letterSpacing = 0.5.sp
          )
          Spacer(modifier = Modifier.height(10.dp))
          activeProtocol.forbiddenFoods.forEach { food ->
            Row(
              verticalAlignment = Alignment.Top,
              modifier = Modifier.padding(vertical = 4.dp)
            ) {
              Text("❌", fontSize = 11.sp, modifier = Modifier.padding(end = 8.dp, top = 2.dp))
              Text(
                text = food,
                fontSize = 12.sp,
                color = CleanWhite.copy(0.9f),
                lineHeight = 16.sp
              )
            }
          }
        }
      }
    }

    // Detailed Samples Meal Plan
    item {
      CustomBorderCard(
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        borderColor = DarkCardBorder,
        borderWidth = 1.dp
      ) {
        Column(modifier = Modifier.padding(20.dp)) {
          Text(
            text = "🍽️ JADŁOSPIS TERAPEUTYCZNY (Zastosowanie Praktyczne)",
            fontSize = 11.sp,
            fontWeight = FontWeight.ExtraBold,
            color = SoftGray,
            letterSpacing = 0.5.sp
          )
          Spacer(modifier = Modifier.height(14.dp))
          activeProtocol.mealPlan.forEachIndexed { index, (mealName, mealDesc) ->
            Row(
              verticalAlignment = Alignment.Top,
              modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 14.dp)
            ) {
              Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                  .size(24.dp)
                  .clip(RoundedCornerShape(50))
                  .background(EnergyAmber.copy(0.12f))
              ) {
                Text(
                  text = "${index + 1}",
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  color = EnergyAmber
                )
              }
              Spacer(modifier = Modifier.width(10.dp))
              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = mealName,
                  fontSize = 13.sp,
                  fontWeight = FontWeight.Bold,
                  color = CleanWhite
                )
                Text(
                  text = mealDesc,
                  fontSize = 12.sp,
                  color = SoftGray,
                  lineHeight = 16.sp
                )
              }
            }
          }
        }
      }
    }

    // Physiological Fact Card (Dr. Ede's voice)
    item {
      CustomBorderCard(
        shape = RoundedCornerShape(24.dp),
        color = BrainPink.copy(0.04f),
        borderColor = BrainPink.copy(0.15f),
        borderWidth = 1.dp
      ) {
        Column(modifier = Modifier.padding(20.dp)) {
          Text(
            text = "🧠 GŁOS DR GEORGII EDE — FIZJOLOGIA",
            fontSize = 11.sp,
            fontWeight = FontWeight.ExtraBold,
            color = BrainPink,
            letterSpacing = 0.5.sp
          )
          Spacer(modifier = Modifier.height(10.dp))
          Text(
            text = activeProtocol.physiologicalFact,
            fontSize = 12.sp,
            color = CleanWhite.copy(0.95f),
            fontWeight = FontWeight.Medium,
            lineHeight = 17.sp
          )
        }
      }
    }

    // ==========================================
    // INTERACTIVE TRANSITION GUIDE CARD
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

          // Transition directions selector
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
                  .border(BorderStroke(1.dp, if (isSelected) EnergyAmber else DarkCardBorder), RoundedCornerShape(12.dp))
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

          // Transition guidance
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
              advicePhysiology = "Fizjologia: Następuje 100% odcięcie substancji roślinnych. Wyeliminuje zostają wbudowane mechanizmy obronne roślin: szkodliwe lektyny, drażniący kwas fitynowy, szczawian i salicylany. Te drobiny uszkadzają barierę jelitową i chronicznie aktywują mikroglej mózgu."
              adviceWarning = "Objawy/Pułapki: Przejściowy ból brzucha (zmiana flory bakteryjnej) oraz proces oxalate dumping (masowe pozbywanie się kryształków szczawianów z tkanek, co może przejściowo objawić się swędzeniem skóry lub pęcherzykami)."
              adviceSteps = "Protokół dr Ede: Bazuj wyłącznie na tłustych częściach wołowiny (antrykot, boczki), podrobach, ekologicznych żółtkach jaj i soli kamiennej. Całkowicie wyłącz kawę i herbatę (to bomby szczawianowe drażniące jelita). Pij wyłącznie czystą wodę."
            }
            2 -> {
              adviceTitle = "KIERUNEK: Quiet Carnivore do Keto/Paleo (Mądra Reintrodukcja)"
              advicePhysiology = "Fizjologia: Po fazie czystego Carnivore neurozapalenie ulega wygaszeniu, a bariera jelitowa uszczelnieniu. Każdy nowo wprowadzany pokarm daje natychmiastowe, nieskażone i czyste sprzężenie zwrotne o reakcjach autoimmunologicznych Twojego mózgu."
              adviceWarning = "Objawy/Pułapki: Wprowadzanie wielu pokarmów roślinnych naraz. Jeśli zjesz miskę sałatki z awokado, pomidorami i pestkami, nie namierzysz, który składnik (salicylany, histamina czy lektyny) wywołał powrót lęków bądź mgły."
              adviceSteps = "Protokół dr Ede: Wybierz jeden bezpieczny element o minimalnej toksyczności (np. czysty obrany ogórek bez nasion lub awokado). Jedz go raz dziennie przez 3 dni z rzedu. Obserwuj tętno, jasność myśli, stany lękowe i sen. Dopiero po 72h wprowadź kolejny składnik."
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
        .padding(horizontal = 8.dp, vertical = 10.dp),
      horizontalArrangement = Arrangement.SpaceAround,
      verticalAlignment = Alignment.CenterVertically
    ) {
      BottomNavTab(icon = "🧠", label = "Symulator", active = currentScreen == MainScreen.SIMULATOR, onClick = { onScreenChange(MainScreen.SIMULATOR) }, testTagId = "nav_simulator")
      BottomNavTab(icon = "📖", label = "Wiedza", active = currentScreen == MainScreen.HOME, onClick = { onScreenChange(MainScreen.HOME) }, testTagId = "nav_home")
      BottomNavTab(icon = "🥗", label = "Protokoły", active = currentScreen == MainScreen.TRACK, onClick = { onScreenChange(MainScreen.TRACK) }, testTagId = "nav_track")
      BottomNavTab(icon = "🎯", label = "Motywacja", active = currentScreen == MainScreen.MOTIVATION, onClick = { onScreenChange(MainScreen.MOTIVATION) }, testTagId = "nav_motivation")
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

@Composable
fun MotivationScreen() {
  var activeSubTab by remember { mutableStateOf(0) }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(DeepDarkBlue)
      .verticalScroll(rememberScrollState())
      .padding(16.dp)
  ) {
    // Header
    Text(
      text = "NAUKA & MOTYWACJA 🎯",
      fontSize = 11.sp,
      fontWeight = FontWeight.ExtraBold,
      color = BrainPink,
      letterSpacing = 0.5.sp
    )
    Spacer(modifier = Modifier.height(2.dp))
    Text(
      text = "Metaboliczne Uwolnienie Mózgu",
      fontSize = 20.sp,
      fontWeight = FontWeight.ExtraBold,
      color = CleanWhite,
      lineHeight = 24.sp
    )
    Spacer(modifier = Modifier.height(6.dp))
    Text(
      text = "To nie brak siły woli generuje kryzys psychiczny – to Twoja biologia bije na alarm. Dr. Ede dowodzi, że zmiana paliwa glukozowego na ciała ketonowe (G-to-K Switch) wycofuje lęki obwodowe i regeneruje neurony.",
      fontSize = 12.sp,
      color = SoftGray,
      lineHeight = 16.sp
    )
    Spacer(modifier = Modifier.height(16.dp))

    // Interactive custom tab row
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .horizontalScroll(rememberScrollState())
        .padding(bottom = 6.dp),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      val tabs = listOf(
        "📖 Historie Sukcesu",
        "🧬 Trzy Fazy Kryzysu",
        "🍋 Pogromca Mitów",
        "🎯 Kalkulator Subtrakcji"
      )
      tabs.forEachIndexed { index, title ->
        val isSelected = activeSubTab == index
        CustomBorderCard(
          shape = RoundedCornerShape(12.dp),
          color = if (isSelected) EnergyAmber else Color.White,
          borderColor = if (isSelected) EnergyAmber else DarkCardBorder,
          borderWidth = 1.dp,
          modifier = Modifier.clickable { activeSubTab = index }
        ) {
          Text(
            text = title,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = if (isSelected) Color.White else CleanWhite,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(12.dp))

    when (activeSubTab) {
      0 -> SuccessCaseStudiesView()
      1 -> MetabolicCrisisProgressionView()
      2 -> MythBusterInteractiveView()
      3 -> SubtractionInteractiveCalculatorView()
    }
    
    Spacer(modifier = Modifier.height(40.dp))
  }
}

@Composable
fun SuccessCaseStudiesView() {
  var selectedCase by remember { mutableStateOf(0) }

  Column {
    Text(
      text = "AUTENTYCZNE HISTORIE KLINICZNE (Wybierz pacjenta)",
      fontSize = 10.sp,
      fontWeight = FontWeight.ExtraBold,
      color = EnergyAmber,
      letterSpacing = 0.5.sp
    )
    Spacer(modifier = Modifier.height(8.dp))

    // Patient horizontal tabs
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .horizontalScroll(rememberScrollState()),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      val patients = listOf("Karl (Bipolar II)", "Fran (Demencja)", "Lisa (Ataki Paniki)", "Deanna (Depresja)")
      patients.forEachIndexed { idx, name ->
        val active = selectedCase == idx
        CustomBorderCard(
          shape = RoundedCornerShape(10.dp),
          color = if (active) EnergyAmber.copy(0.08f) else Color.White,
          borderColor = if (active) EnergyAmber else DarkCardBorder,
          borderWidth = 1.dp,
          modifier = Modifier.clickable { selectedCase = idx }
        ) {
          Text(
            text = name,
            fontSize = 11.sp,
            fontWeight = if (active) FontWeight.Bold else FontWeight.Medium,
            color = if (active) EnergyAmber else SoftGray,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(12.dp))

    val (quote, text, oldDiet, newDiet, bioImpact) = when (selectedCase) {
      0 -> listOf(
        "„Po 39 dniach na nowej diecie lęki nocne i bezsenność całkowicie wygasły (wynik zero!). Odstawiłem wszystkie psychotropy.”",
        "Karl przez kilkanaście lat cierpiał na potężną bezsenność i stany maniakalne (zmuszające go do nocnych marszów po 40 kilometrów!). Przeszedł nieskuteczne terapie chłodzące głowę farmakologicznie, zdiagnozowano u niego ciężką dwubiegunowość (Bipolar II).",
        "Standardowa Dieta Amerykańska (Chleb, drożdżówki, piwo, frytki, rafinowane oleje rzepakowe/sojowe).",
        "Quiet Carnivore (Wyłącznie świeża tłusta wołowina, ryby, woda gazowana i sól morska bez roślinnych lektyn).",
        "Ominięcie Kompleksu I: Poziom ciężkiego lęku (GAD-7) spadł z 17 na 0! Mitochondria odzyskały stabilne zasilanie, odrzucając potrzebę pigułek."
      )
      1 -> listOf(
        "„Wróciła mi zdolność mowy, kojarzenia i wątków myślowych. Neurolog z zachwytem zdjął ze mnie diagnozę Alzheimera!”",
        "Fran, emerytowana dietetyczka, uległa wypadkowi i doznała ucisku hipokampa. Po 60-tce traciła mowę, nie mogła nawlec maszyny do szycia i gubiła się na drodze. Lekarz kazał jej iść do domu i 'cieszyć się resztą dni'.",
        "Wysoka w skrobie (płatki otrębowe, chudy drób, margaryny rzekomo zdrowe dla serca i tłuszcze trans).",
        "Quiet Keto (Fasty nocne min. 16h, olej kokosowy w rosole, sałaty z oliwą, tłuste ryby bez kazeiny mlecznej).",
        "Zasilenie przez Kompleks II: Mitochondria hipokampa obeszły uszkodzony Kompleks I. Wynik neurologiczny wzrósł do 27/28!"
      )
      2 -> listOf(
        "„Byłam nękana panicznym lękiem od 17. roku życia. Zjadłam kilogramy Lexapro i Klonopinu, a przyczyną były... jajka!”",
        "Lisa cierpiała na fobie lękowe przez cztery dekady. Tradycyjne testy alergiczne dały fałszywy wynik ujemny. Pewnego dnia przed wizytą zjadła tylko ugotowane jajko i doznała natychmiastowego ataku paniki.",
        "Wegetariańska (Dużo jaj kurzych, odtłuszczone sery maziowe, płatki i rafinowane oleje nasienne).",
        "Strict Quiet Carnivore (Tłuste czerwone mięso, wycięcie kurzych białek, jaj i nabiału na 30 dni).",
        "Wrażliwość pozajelitowa: Odkryła rzadką nietolerancję albuminy jaj kurzych wywołującą potężne lęki i zapalenie mikrogleju."
      )
      else -> listOf(
        "„Moja kora została dosłownie zalana naturalnym zasileniem. Cukier przestał kontrolować moje myśli w 12 dni.”",
        "Deanna (64 lata) zmagała się z głęboką głodówką neuronalną, ciągłym objadaniem się i depresją kliniczną (skrajne 37 punktów w skali Becka). Była zbyt wyczerpana psychicznie, by liczyć makroskładniki.",
        "Cukry rafinowane, chipsy ziemniaczane ze smażalni, fast food podjadany od 16:00 do nocy.",
        "Quiet Paleo oparte na bezwzględnej eliminacji (mięso naturalne, woda bez podjadania między posiłkami).",
        "Ketoza w 4 dni: Poziom ciał ketonowych BHB wzrósł do 2.1 mM, a depresja cofnęła się całkowicie (BDI spadł ze skrajnego 37 do 7)!"
      )
    }

    CustomBorderCard(
      shape = RoundedCornerShape(20.dp),
      color = Color.White,
      borderColor = DarkCardBorder,
      borderWidth = 1.dp,
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(18.dp)) {
        Text(
          text = quote,
          color = EnergyAmber,
          fontSize = 13.sp,
          fontWeight = FontWeight.ExtraBold,
          fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
          lineHeight = 18.sp
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
          text = text,
          fontSize = 12.sp,
          color = CleanWhite,
          lineHeight = 16.sp
        )
        Spacer(modifier = Modifier.height(12.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
          Column(modifier = Modifier.weight(1f)) {
            Text("⚙️ SYGNATURA SAD (Stary Błąd):", fontSize = 10.sp, fontWeight = FontWeight.ExtraBold, color = BrainPink)
            Spacer(modifier = Modifier.height(2.dp))
            Text(oldDiet, fontSize = 11.sp, color = CleanWhite.copy(0.8f), lineHeight = 14.sp)
          }
          Column(modifier = Modifier.weight(1f)) {
            Text("🌱 BAZA QUIET DIET (Rozwiązanie):", fontSize = 10.sp, fontWeight = FontWeight.ExtraBold, color = KetoneCyan)
            Spacer(modifier = Modifier.height(2.dp))
            Text(newDiet, fontSize = 11.sp, color = CleanWhite.copy(0.8f), lineHeight = 14.sp)
          }
        }
        Spacer(modifier = Modifier.height(12.dp))
        StyledDivider()
        Spacer(modifier = Modifier.height(10.dp))
        Text(
          text = "🧬 BIOLOGICZNY MECHANIZM COFNIĘCIA CHOROBY:",
          fontSize = 10.sp,
          fontWeight = FontWeight.ExtraBold,
          color = CleanWhite,
          letterSpacing = 0.5.sp
        )
        Spacer(modifier = Modifier.height(3.dp))
        Text(
          text = bioImpact,
          fontSize = 11.sp,
          color = SoftGray,
          lineHeight = 14.sp
        )
      }
    }
  }
}

@Composable
fun MetabolicCrisisProgressionView() {
  var selectedPhase by remember { mutableStateOf(0) }

  Column {
    Text(
      text = "KROKI DEGRADACJI METABOLICZNEJ MÓZGU (Wybierz fazę)",
      fontSize = 10.sp,
      fontWeight = FontWeight.ExtraBold,
      color = BrainPink,
      letterSpacing = 0.5.sp
    )
    Spacer(modifier = Modifier.height(8.dp))

    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      val stages = listOf("Faza I\nNiestabilność", "Faza II\nUkryta Oporność", "Faza III\nZagłodzenie (T3D)")
      stages.forEachIndexed { index, name ->
        val isActive = selectedPhase == index
        CustomBorderCard(
          shape = RoundedCornerShape(12.dp),
          color = if (isActive) BrainPink.copy(0.04f) else Color.White,
          borderColor = if (isActive) BrainPink else DarkCardBorder,
          borderWidth = 1.dp,
          modifier = Modifier
            .weight(1f)
            .clickable { selectedPhase = index }
        ) {
          Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Text(
              text = "0${index + 1}",
              fontSize = 13.sp,
              fontWeight = FontWeight.ExtraBold,
              color = if (isActive) BrainPink else SoftGray
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
              text = name,
              fontSize = 9.sp,
              fontWeight = FontWeight.Bold,
              color = CleanWhite,
              textAlign = TextAlign.Center,
              lineHeight = 11.sp
            )
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(12.dp))

    val (title, sub, description, alertText, recoveryKey) = when (selectedPhase) {
      0 -> listOf(
        "Faza I: Huśtawka Adrenalinowa (Metabolic Instability)",
        "Początek drogi do chronicznej paniki",
        "Wysokie spożycie rafinowanych cukrów i zbóż wywołuje strome piki glukozy. Trzustka w panice wyrzuca gigantyczne fale insuliny, by ubić toksyczny nadmiar cukru. Gdy poziom glukozy pikuje zbyt drastycznie, głodny mózg interpretuje to jako alarm śmierci głodowej i zalewa krew hormonami stresu: adrenaliną i kortyzolem.",
        "OBJAW: Ataki paniki znikąd, shakiness (drżenie rąk i poty), rozbicie umysłowe, nagłe poczucie strachu i bezsenność.",
        "ROZWIĄZANIE DR EDE: Quiet Paleo. Usunięcie rafinacji stabilizuje poziom glukozy obwodowej, dając natychmiastowe wyciszenie hormonów w ciągu 1-5 dni!"
      )
      1 -> listOf(
        "Faza II: Cicha Epidemia (High Insulin, Normal Glucose)",
        "Mecz o metaboliczną odporność komórek trwa 10-20 lat",
        "Neurony bronią się przed permanentną powodzią cukru i insuliny, wyłączając i blokując aktywne receptory. Powstaje ukryta insulinooporność. Wynik glukozy u lekarza wychodzi idealnie w normie, ale ukrytym kosztem jest to, że trzustka produkuje 5x-10x więcej insuliny, by ten stan utrzymać.",
        "ZAGROŻENIE: IDE (Insulin Degrading Enzyme) rzuca wszystkie siły na utylizację nadmiaru insuliny i przestaje oczyszczać mózg ze złogów Amyloidu i Tau!",
        "ROZWIĄZANIE DR EDE: Quiet Keto. Przejście na spalanie tłuszczu obniża poziom insuliny do jednocyfrowych liczb, zwalniając IDE do oczyszczenia synaps."
      )
      else -> listOf(
        "Faza III: Mózgowa Cukrzyca (Cerebral Glucose Hypometabolism)",
        "Neuron umiera z głodu w oceanie niewykorzystanego cukru",
        "Permanentnie podwyższona insulina deaktywuje receptory na barierze krew-mózg. Insulina nie jest w stanie wniknąć do neuronów, by uruchomić spalanie glukozy. Pompa sodowo-potasowa w neuronach przestaje działać z braku ATP (potrzebuje 4 mld ATP na sekundę!). Mózg przechodzi cichy brownout i dosłownie kurczy się pod wpływem glukotoksyczności.",
        "FAKT: Aż 81% pacjentów z Alzheimerem ma zaawansowaną insulinooporność (Cukrzyca Typu 3). Stany te wyprzedzają objawy utraty pamięci o 30 lat!",
        "ROZWIĄZANIE DR EDE: Quiet Carnivore / Keto. Wątroba z lipidów produkuje ciała ketonowe (BHB), które swobodnie wnikają do neuronu omijając blokadę insuliny!"
      )
    }

    CustomBorderCard(
      shape = RoundedCornerShape(20.dp),
      color = Color.White,
      borderColor = DarkCardBorder,
      borderWidth = 1.dp,
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(18.dp)) {
        Text(text = title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = CleanWhite)
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = sub, fontSize = 11.sp, color = BrainPink, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = description, fontSize = 12.sp, color = CleanWhite.copy(0.85f), lineHeight = 16.sp)
        Spacer(modifier = Modifier.height(12.dp))

        CustomBorderCard(
          shape = RoundedCornerShape(10.dp),
          color = BrainPink.copy(0.05f),
          borderColor = BrainPink.copy(0.12f),
          borderWidth = 1.dp,
          modifier = Modifier.fillMaxWidth()
        ) {
          Text(
            text = alertText,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = BrainPink,
            lineHeight = 14.sp,
            modifier = Modifier.padding(10.dp)
          )
        }
        Spacer(modifier = Modifier.height(10.dp))
        CustomBorderCard(
          shape = RoundedCornerShape(10.dp),
          color = KetoneCyan.copy(0.05f),
          borderColor = KetoneCyan.copy(0.12f),
          borderWidth = 1.dp,
          modifier = Modifier.fillMaxWidth()
        ) {
          Text(
            text = recoveryKey,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = KetoneCyan,
            lineHeight = 14.sp,
            modifier = Modifier.padding(10.dp)
          )
        }
      }
    }
  }
}

@Composable
fun MythBusterInteractiveView() {
  var activeMyth by remember { mutableStateOf(-1) }

  val myths = listOf(
    Pair("1. Borówki (Blueberries) stymulują pamięć i mózg", "DEKLASACJA MITU:\nWszystkie rzekome korzyści z borówek pochodzą z małych testów in vitro na wyizolowanych polifenolach. W żywym organizmie wchłanianie anthocyanins wynosi poniżej 1%, a wątroba natychmiast wychwytuje je i wydala jako niepotrzebne ksenobiotyki. Zdrowsza jest truskawka (7x więcej witaminy C i o połowę mniej cukru!). USDA usunął bazę danych ORAC w 2012 roku, przyznając, że to marketingowy mit!"),
    Pair("2. Czerwone wino chroni serce resweratrolem", "DEKLASACJA MITU:\nAby uzyskać terapeutyczną dawkę resweratrolu wykazaną w badaniach klinicznych, musiałbyś pić... 500 butelek wina dziennie! Każda szklanka wina zalewa Twoją wątrobę i mózg neurotoksycznym alkoholem, który zmusza wątrobę do wstrzymania produkcji glukozy i spalania tłuszczu, wywołując potężny stres oksydacyjny."),
    Pair("3. Pełnoziarniste pieczywo i zboża to podstawa", "DEKLASACJA MITU:\nMedykalizacja ziaren zbóż jako podstawy to błąd. Ziarna to uśpione zarodki traw, które chronią się chemiczną zbroją. Zawierają agresywne lektyny (np. WGA w pszenicy) oraz kwas fitynowy, które fizycznie dziurawią błonę jelitową i blokują wchłanianie cynku, żelaza, magnezu i miedzi potrzebnych do syntezy serotoniny i dopaminy."),
    Pair("4. Sulforafan w surowym brokule chroni mózg", "DEKLASACJA MITU:\nSulforafan to silna toksyna obronna ( insecticide nazywana 'bombą musztardową'). Roślina trzyma jego składniki oddzielnie. Gdy gryziesz brokuł, na skutek zniszczenia komórek powstaje sulforafan, by uszkodzić przewód pokarmowy agresora. Organizm człowieka stara się go natychmiast wydalić, a rzekome korzyści to jedynie pobudzony do obrony układ detoksykacji wątroby!")
  )

  Column {
    Text(
      text = "OBALENIE MITÓW MARKETINGOWYCH (Kliknij, aby odkryć prawdę)",
      fontSize = 10.sp,
      fontWeight = FontWeight.ExtraBold,
      color = EnergyAmber,
      letterSpacing = 0.5.sp
    )
    Spacer(modifier = Modifier.height(8.dp))

    myths.forEachIndexed { index, pair ->
      val isOpen = activeMyth == index
      CustomBorderCard(
        shape = RoundedCornerShape(12.dp),
        color = if (isOpen) EnergyAmber.copy(0.03f) else Color.White,
        borderColor = if (isOpen) EnergyAmber else DarkCardBorder,
        borderWidth = 1.dp,
        modifier = Modifier
          .fillMaxWidth()
          .clickable { activeMyth = if (isOpen) -1 else index }
          .padding(vertical = 4.dp)
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = pair.first,
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              color = if (isOpen) EnergyAmber else CleanWhite,
              modifier = Modifier.weight(0.8f)
            )
            Text(
              text = if (isOpen) "▲ ZAMKNIJ" else "▼ PRAWDA",
              fontSize = 9.sp,
              fontWeight = FontWeight.Bold,
              color = SoftGray,
              modifier = Modifier.weight(0.2f),
              textAlign = TextAlign.End
            )
          }
          if (isOpen) {
            Spacer(modifier = Modifier.height(8.dp))
            StyledDivider()
            Spacer(modifier = Modifier.height(8.dp))
            Text(
              text = pair.second,
              fontSize = 11.sp,
              color = CleanWhite.copy(0.85f),
              lineHeight = 15.sp
            )
          }
        }
      }
    }
  }
}

@Composable
fun SubtractionInteractiveCalculatorView() {
  val checkList = remember {
    mutableStateListOf(
      Pair("Rafinowany cukier (Zapobieganie pikom glukozy)", false),
      Pair("Płynne kalorie (Soki owocowe, koktajle, słodzona kawa)", false),
      Pair("Zboża i gluten (Ochrona bariery jelitowej i minerałów)", false),
      Pair("Alkool i wino (Koniec z zalewaniem wątroby)", false),
      Pair("Olej rzepakowy/nasienny (Koniec niszczenia kardiolipiny)", false),
      Pair("Podjadanie między posiłkami (Czas dla odpoczynku jelit)", false),
      Pair("Przetworzona żywność 'Keto-friendly' ze słodzikami", false),
      Pair("Nocne okno postu min. 16h (Samoczyszczenie neuronów)", false)
    )
  }

  val removedCount = checkList.count { it.second }
  val protectionScore = (removedCount / 8f) * 100

  Column {
    Text(
      text = "UZDROWIENIE TO SUBTRAKCJA (Odejmij, aby ocalić mózg)",
      fontSize = 10.sp,
      fontWeight = FontWeight.ExtraBold,
      color = EnergyAmber,
      letterSpacing = 0.5.sp
    )
    Spacer(modifier = Modifier.height(8.dp))

    CustomBorderCard(
      shape = RoundedCornerShape(20.dp),
      color = Color.White,
      borderColor = DarkCardBorder,
      borderWidth = 1.dp,
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(18.dp)) {
        Text(
          text = "Prawdziwa Tarcza Ochronna Mózgu 🛡️",
          fontSize = 14.sp,
          fontWeight = FontWeight.Bold,
          color = CleanWhite
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = "Medycyna marketingowa radzi dodawać 'superfoods'. Dr. Georgia Ede uczy, że powrót do zdrowia to SUBTRAKCJA niszczących czynników. Zaznacz elementy do wycięcia:",
          fontSize = 11.sp,
          color = SoftGray,
          lineHeight = 15.sp
        )
        Spacer(modifier = Modifier.height(14.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Wskaźnik Regeneracji Mitochondriów:",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = CleanWhite
          )
          Text(
            text = "${protectionScore.toInt()}%",
            fontSize = 16.sp,
            fontWeight = FontWeight.ExtraBold,
            color = if (protectionScore < 40) BrainPink else if (protectionScore < 80) EnergyAmber else KetoneCyan
          )
        }
        Spacer(modifier = Modifier.height(6.dp))
        LinearProgressIndicator(
          progress = removedCount / 8f,
          color = if (protectionScore < 40) BrainPink else if (protectionScore < 80) EnergyAmber else KetoneCyan,
          trackColor = Color(0xFFF1F5F9),
          modifier = Modifier
            .fillMaxWidth()
            .height(10.dp)
            .clip(RoundedCornerShape(5.dp))
        )

        Spacer(modifier = Modifier.height(12.dp))

        val feedback = when {
          removedCount == 0 -> "Brak hamulców. Mózg znajduje się w stanie permanetnego chaosu i zablokowanej energii z glukozy."
          removedCount <= 3 -> "Pierwsze komórki odzyskują tlen, ale wysoka podaż Omega-6 wciąż wywołuje rany mitochondrialne."
          removedCount <= 6 -> "Świetna robota! Poziom zapaleń kynureninowych drastycznie spada. Neurony dostają potężny zastrzyk energii."
          else -> "Pełny dobrostan! Osiągasz szczyt ochrony metabolicznej. Twoje mitochondria działają jak sprawny, tlenowy reaktor!"
        }

        CustomBorderCard(
          shape = RoundedCornerShape(12.dp),
          color = if (removedCount <= 3) BrainPink.copy(0.04f) else KetoneCyan.copy(0.04f),
          borderColor = if (removedCount <= 3) BrainPink.copy(0.12f) else KetoneCyan.copy(0.12f),
          borderWidth = 1.dp,
          modifier = Modifier.fillMaxWidth()
        ) {
          Text(
            text = "ODPOWIEDŹ METABOLICZNA:\n$feedback",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = if (removedCount <= 3) BrainPink else KetoneCyan,
            lineHeight = 15.sp,
            modifier = Modifier.padding(10.dp)
          )
        }

        Spacer(modifier = Modifier.height(14.dp))
        StyledDivider()
        Spacer(modifier = Modifier.height(10.dp))

        // Checkbox layout
        checkList.forEachIndexed { index, pair ->
          val isChecked = pair.second
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clickable { checkList[index] = Pair(pair.first, !isChecked) }
              .padding(vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            CustomBorderCard(
              shape = RoundedCornerShape(4.dp),
              color = if (isChecked) EnergyAmber else Color.White,
              borderColor = if (isChecked) EnergyAmber else SoftGray,
              borderWidth = 1.5.dp,
              modifier = Modifier.size(18.dp)
            ) {
              if (isChecked) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                  Text("✓", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
              }
            }
            Spacer(modifier = Modifier.width(10.dp))
            Text(
              text = pair.first,
              fontSize = 12.sp,
              color = if (isChecked) CleanWhite else CleanWhite.copy(0.75f),
              fontWeight = if (isChecked) FontWeight.Bold else FontWeight.Normal
            )
          }
        }
      }
    }
  }
}

