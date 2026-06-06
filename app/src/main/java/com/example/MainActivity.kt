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
        subtitle = "Głodówka energetyczna mózgu przy zablokowaniu cukru",
        bulletPoints = listOf(
          "Aby komórki mózgowe mogły skutecznie przetwarzać glukozę, niezbędna jest wysoka wrażliwość receptorów dla insuliny znajdujących się na synapsach i obrzeżach bariery krew-mózg.",
          "Chroniczna dominacja węglowodanów w diecie (chleby, kasze, cukry proste, nadmiar słodkiej fruktozy) prowadzi do ogłuszenia i zablokowania tych receptorów (organizm traci na nie wrażliwość).",
          "Wychwyt glukozy ulega całkowitemu zablokowaniu. Powstaje mózgowa insulinooporność: cukier zalega w krwi obwodowej, ale niewrażliwe komórki mózgowe dosłownie umierają z głodu.",
          "Dodatkowo wolne rodniki z nadmiaru cukru łączą się z białkami i 'karmelizują' je. Powstają toksyczne, uszkodzone cząsteczki (końcowe produkty zaawansowanej glikacji), które niszczą hipokamp i prowadzą do demencji (tzw. cukrzycy typu 3)."
        ),
        shockingTakeaway = "SZOKUJĄCY FAKT: Nawet osoba z idealnym poziomem cukru u lekarza może cierpieć na zaawansowaną oporność insulinową w mózgu. Głodny mózg rozpaczliwie krzyczy o energię, wywołując silne stany lękowe, panikę i wilczy głód."
      ),
      DetailSection(
        id = "bio_5",
        title = "5. Ketony jako Rewolucyjny Bypass",
        subtitle = "Ominięcie uszkodzonych ścieżek i ratunek dla komórek",
        bulletPoints = listOf(
          "Podczas braku węglowodanów poziom hormonu insuliny spada, uwalniając proces spalania tłuszczów. Wątroba produkuje z nich zbawienne ciała ketonowe: naturalny ocalający mózg beta-hydroksymaślan oraz acetooctan.",
          "Przekształcenie glukozy w podstawowe paliwo komórkowe wymaga aż 13 skomplikowanych reakcji chemicznych. Przekształcenie czystego ketonu w to samo paliwo wymaga zaledwie 3 prostych kroków!",
          "Większość patologii psychicznych (choroba dwubiegunowa, schizofrenia, depresja lekooporna) cechuje się wrodzonym lub nabytym uszkodzeniem pierwszego generatora energii w mitochondriach (tzw. Kompleksu I).",
          "Podczas gdy glukoza ma całkowicie zablokowaną drogę przez to uszkodzenie, ciała ketonowe dostarczają elektrony bezpośrednio do drugiego generatora (Kompleksu II), całkowicie omijając ten krytyczny problem!",
          "Dodatkowo ciała ketonowe nie potrzebują insuliny, by wniknąć i zostać spalone w komórkach mózgu, nasycając neurony stabilną, czystą energią."
        ),
        shockingTakeaway = "SZOKUJĄCY FAKT: Ciała ketonowe działają jak biologiczny instalator omijający awarię elektrowni. Dostarczają czystego, bezpiecznego prądu wprost do głodujących komórek bez użycia insuliny!"
      ),
      DetailSection(
        id = "bio_6",
        title = "6. Porwanie Tryptofanu i Szlak Kynureninowy",
        subtitle = "Jak stany zapalne kradną serotoninę i melatoninę",
        bulletPoints = listOf(
          "W zdrowych warunkach mózg przekształca aminokwas tryptofan w hormon szczęścia (serotoninę) oraz hormon głębokiego snu (melatoninę).",
          "Pod wpływem przewlekłego zapalenia i wolnych rodników (wywołanych przez cukry i oleje roślinne), tryptofan zostaje porwany ze szlaku szczęścia na szkodliwą, neurotoksyczną ścieżkę kynureninową.",
          "Skutkuje to drastycznym niedoborem serotoniny i melatoniny, połączonym z uderzeniowym, gwałtownym wzrostem produkcji toksycznego kwasu glutaminowego w mózgu.",
          "Nadmiar kwasu glutaminowego drażni i dławi odbiorniki pobudzenia nerwowego (receptory NMDA), powodując śmierć połączeń synaptycznych, uszkodzenie mitochondriów i chroniczne, silne lęki."
        ),
        shockingTakeaway = "SZOKUJĄCY FAKT: Ciągły lęk i bezsenność w depresji to nie brak tabletek w organizmie, lecz fizyczne porwanie tryptofanu pod wpływem jedzenia wywołującego stany zapalne (cukru i olejów nasiennych)!"
      )
    )
  }

  val methodologySections = remember {
    listOf(
      DetailSection(
        id = "met_1",
        title = "1. Piana z Mleka: Epidemiologia Żywieniowa",
        subtitle = "Ankiety pamięciowe to fundament z piasku",
        bulletPoints = listOf(
          "Większość zaleceń karzących unikać tłuszczów zwierzęcych bazuje na badaniach obserwacyjnych korzystających z niefachowych ankiet częstotliwości spożywania pokarmów.",
          "Ankiety te pytają ludzi np.: 'ile razy zjadłeś porcję borówek, brukselki lub boczku w ciągu ostatnich 365 dni?'. Zakłada to perfekcyjną, wręcz niemożliwą pamięć u badanych.",
          "Dowiedziono naukowo, że badani oszukują w ankietach (ze względu na chęć wypadnięcia dobrze przed badaczem): zaniżają spożycie masła, mięsa czy chipsów o połowę, a zawyżają spożycie sałatek.",
          "Kwestionariusze zbierane są skrajnie rzadko (np. tylko 4 razy na przestrzeni 20 lat). Następnie modele matematyczne po prostu udają, że nawyki te były idealnie niezmienne przez całe życie."
        ),
        shockingTakeaway = "SZOKUJĄCY FAKT: Niemal połowa badanych osób przyznaje, że kłamie lub po prostu zgaduje odpowiedzi w ankietach żywieniowych. Współczesna oficjalna piramida żywieniowa spoczywa na tak niewiarygodnych danych!"
      ),
      DetailSection(
        id = "met_2",
        title = "2. Współwystępowanie to NIE Przyczynowość",
        subtitle = "Błąd precla i uprzedzenie zdrowego stylu życia",
        bulletPoints = listOf(
          "W statystykach żywieniowych nagminnie myli się zwykłe współwystępowanie z bezpośrednim związkiem przyczynowo-skutkowym. To klasyczny 'błąd słonego precla'.",
          "Skoro klienci jedzący słone precle w barach częściej mają chorą wątrobę, badacz mógłby ogłosić, że precle niszczą wątrobę – ignorując fakt, że klienci popijali je ogromną ilością alkoholu.",
          "W badaniach nad czerwonym mięsem wystąpiło ogromne uprzedzenie zwane błędem zdrowego użytkownika (Healthy User Bias). Ponieważ w latach 80. mięso zostało okrzyknięte szkodliwym, osoby dbające o zdrowie przestały je jeść.",
          "Osoby dbające o siebie jadły mniej mięsa, ale też uprawiały sport, nie piły i nie paliły. Z kolei osoby jedzące mięso częściej paliły papierosy, piły alkohol, nie ćwiczyły i podjadały chipsy.",
          "Współczesne badania oskarżają mięso, ignorując fakt, że w tamtych latach jedzenie czerwonego mięsa było po prostu oznaką gorszego ogólnego dbania o zdrowie."
        ),
        shockingTakeaway = "SZOKUJĄCY FAKT: Badania ankietowe potrafią tworzyć jedynie domysły naukowe. Nigdy nie udowodniły i nie udowodnią, że naturalny tłuszcz nasycony bezpośrednio zatyka tętnice człowieka."
      ),
      DetailSection(
        id = "met_3",
        title = "3. Kryteria Bradforda Hilla: Co jest Prawdziwą Nauką?",
        subtitle = "Szum statystyczny zamiast twardych dowodów",
        bulletPoints = listOf(
          "W 1965 roku wybitny badacz sir Austin Bradford Hill stworzył 9 kryteriów pozwalających odróżnić korelację od prawdziwego wpływu. Kluczowa jest siła powiązania statystycznego (tzw. Ryzyko Względne).",
          "W badaniach nad wpływem palenia papierosów na raka płuc, ryzyko względne wynosiło od 10.0 do 30.0 (palacze chorują 10-30 razy częściej!). Związek przyczynowy jest bezdyskusyjny.",
          "W rzetelnej nauce przyjmuje się, że badanie ankietowe powinno wykazać wzrost ryzyka przynajmniej o 100% (współczynnik powyżej 2.0), aby brać je poważnie pod uwagę.",
          "Większość nagłówków straszących jedzeniem wykazuje wzrost ryzyka na poziomie zaledwie 5% do 20% (współczynnik 1.05 do 1.2), co w rzetelnej nauce jest zwykłym szumem pomiarowym i błędem statystycznym."
        ),
        shockingTakeaway = "SZOKUJĄCY FAKT: Ryzyko rzędu 1.15 jest matematycznie niemożliwe do oddzielenia od zwykłego błędu w ankiecie. Mimo to prasa przedstawia je jako twardy dowód na szkodliwość masła, czerwonego mięsa czy jajek!"
      ),
      DetailSection(
        id = "met_4",
        title = "4. Przebiegły Eksperyment Aniczkowa na Królikach",
        subtitle = "Błąd zmuszania roślinożercy do jedzenia mięsa",
        bulletPoints = listOf(
          "W 1913 roku badacz Nikołaj Aniczkow wywołał uszkodzenia naczyń u królików, podając im w jedzeniu ogromne dawki wyizolowanego czystego cholesterolu.",
          "To stare badanie stało się głównym fundamentem teorii cholesterolowej grożącej nam chorobami serca po jajkach i maśle.",
          "Zasadniczy błąd: Królik to ścisły roślinożerca! Jego wątroba, jelita i enzymy nie posiadają biochemicznych narzędzi do trawienia pokarmów odzwierzęcych.",
          "Gdy dokładnie ten sam eksperyment powtórzono na świniach, szczurach, psach i innych organizmach wszystkożernych (takich jak człowiek), ich ciała bez problemu przyswoiły cholesterol, a ich naczynia pozostały idealnie czyste i zdrowe."
        ),
        shockingTakeaway = "SZOKUJĄCY FAKT: Cała współczesna walka z masłem i jajkami wywodzi się z badania, w którym roślinożerny królik po prostu rozchorował się po podaniu mu jedzenia przeznaczonego wyłącznie dla drapieżników!"
      ),
      DetailSection(
        id = "met_5",
        title = "5. Ciemna Strona Antyoksydantów i Wybielanie Roślin",
        subtitle = "Hormeza, naturalna obrona roślin i fiasko testów w próbówkach",
        bulletPoints = listOf(
          "Przez lata wmawiano nam zbawienne działanie antyoksydantów roślinnych. Słynny ranking zdolności pochłaniania wolnych rodników (testy ORAC) napędzał potężny biznes suplementów.",
          "W 2012 roku Departament Rolnictwa USA (USDA) oficjalnie wycofał tę bazę danych, przyznając publicznie, że testy w próbówkach nie mają żadnego przełożenia na zdrowie żywego człowieka.",
          "Co gorsza, duże, wiarygodne badania kliniczne (RCT) na ludziach wykazały, że sztuczne podawanie dużych dawek witamin C, E i beta-karotenu zwiększyło ryzyko raka i ogólną śmiertelność!",
          "Rośliny bronią się przed zjedzeniem przez owady i ssaki za pomocą chemicznych toksyn (substancji obcych dla organizmu, tzw. ksenobiotyków). Nasza wątroba dąży do ich jak najszybszego usunięcia.",
          "Ewentualne korzyści (wrażenie stymulacji) wynikają z tego, że organizm mobilizuje siły obronne, by usunąć te delikatne roślinne trucizny, a nie z ich bezpośredniego działania oczyszczającego."
        ),
        shockingTakeaway = "SZOKUJĄCY FAKT: Nasz mózg nie potrzebuje bomb w postaci substancji obcych z roślin. Pragnie stabilnego, czystego paliwa w postaci ketonów oraz łatwo przyswajalnych minerałów z tłuszczów zwierzęcych."
      )
    )
  }

  val dietsClinicalSections = remember {
    listOf(
      DetailSection(
        id = "clin_1",
        title = "1. ZABURZENIA LĘKOWE (Stany lękowe i panika)",
        subtitle = "Jak ciała ketonowe odbudowują spokój i wyciszają nerwy podświadome",
        bulletPoints = listOf(
          "Stany lękowe wynikają z nadmiernej pobudliwości jądra migdałowatego (to część mózgu odpowiedzialna za wyczuwanie zagrożenia), które przy brakach podstawowej energii rzuca ciało w tryb alarmowy.",
          "Ciała ketonowe stymulują pracę enzymów komórkowych, które błyskawicznie zamieniają toksyczny i pobudzający kwas glutaminowy (wywołujący lęk) w bardzo uspokajający prąd (kwas gamma-aminomasłowy wyciszający głowę).",
          "Diety eliminacyjne usuwają z jadłospisu chemiczne związki obronne roślin oraz nadmiar zbóż z glutenem, które dziurawią delikatną barierę jelitową, chroniąc przed przedostaniem się toksycznych substancji bakteryjnych do krwiobiegu.",
          "Standardowa dieta dopuszczająca orzechy czy zboża często blokuje wchłanianie minerałów, dlatego silny lęk najlepiej wycisza czyste biologicznie jedzenie oparte na dozwolonych tłuszczach zwierzęcych i eliminacji czynników drażniących."
        ),
        shockingTakeaway = "WSKAZÓWKA DR EDE: Przy silnych stanach lękowych nie obawiaj się czasowego odrzucenia warzyw i owoców. Radykalna rezygnacja z roślin na okres 2-3 tygodni potrafi uspokoić i wygasić niemal wszystkie przewlekłe lęki obwodowe!"
      ),
      DetailSection(
        id = "clin_2",
        title = "2. DEPRESJA I MGŁA MÓZGOWA",
        subtitle = "Mitochondrialna tarcza chroniąca przed zapaleniem komórek",
        bulletPoints = listOf(
          "Współczesna nauka uważa depresję kliniczną za fizyczny stan zapalny delikatnej tkanki nerwowej w mózgu.",
          "Komórki mózgowe przeciążone ciągłymi wahaniami cukru i insuliny zaczynają uwalniać niszczące substancje zapalne. Ciała ketonowe blokują te procesy u samego źródła, wygaszając pożar w głowie.",
          "Zdrowy tłuszcz zwierzęcy oraz naturalny cholesterol są niezbędne do odbudowy otoczek izolujących nerwy, co pozwala na sprawny i bezbłędny przepływ informacji, likwidując zniecierpliwienie i chroniczną apatię.",
          "Ciągłe spadki i piki cukru po zjedzeniu węglowodanów zmuszają ciało do nagłych wyrzutów hormonów stresu (adrenaliny i kortyzolu) o poranku, podsycając płaczliwość i lęk. Stała ketoza całkowicie ułatwia stabilizację emocjonalną."
        ),
        shockingTakeaway = "WSKAZÓWKA DR EDE: Depresja, na którą nie działają żadne tradycyjne leki, rewelacyjnie poddaje się terapii dietą ketogenną opartą na czerwonym mięsie, jajach i zdrowym maśle klarowanym."
      ),
      DetailSection(
        id = "clin_3",
        title = "3. CHOROBA DWUBIEGUNOWA (Ciągłe huśtawki nastroju)",
        subtitle = "Przywrócenie stabilności elektrycznej synaps",
        bulletPoints = listOf(
          "Choroba dwubiegunowa odznacza się głębokimi zaburzeniami poziomu elektrolitów na błonach komórek nerwowych, co popycha mózg od stanów skrajnej manii i pobudzenia do potwornej depresji.",
          "Większość tradycyjnych leków stabilizujących nastrój (np. preparaty litu) działa właśnie na naprawę pomp sodowo-potasowych w komórkach.",
          "Ciała ketonowe działają naturalnie w identyczny sposób jak te leki: nasycają mózg stabilną energią, umożliwiają lekką i bezwysiłkową pracę pomp komórkowych i stabilizują elektryczność mózgu.",
          "Uszkodzenia głównego generatora energii w komórkach zostają całkowicie ominięte, ponieważ ciała ketonowe trafiają bezpośrednio do drugiego, w pełni sprawnego reaktora pomocniczego.",
          "Wybitni badacze z Uniwersytetu Harvarda opisują spektakularne, całkowite cofnięcie i remisje choroby dwubiegunowej u pacjentów żywiących się według zasad czystej diety ketogennej."
        ),
        shockingTakeaway = "WSKAZÓWKA DR EDE: Ciężkie wahania nastroju wymagają precyzyjnego utrzymywania stałego stanu ketozy (uzyskiwanego przez odcięcie cukru i napojów gazowanych), aby mózg cały czas miał doskonałe, alternatywne paliwo."
      ),
      DetailSection(
        id = "clin_4",
        title = "4. ADHD I TRUDNOŚCI Z SKUPIENIEM (Nadpobudliwość)",
        subtitle = "Zapewnienie stałego zasilania dla płatów czołowych",
        bulletPoints = listOf(
          "Obszary mózgu odpowiedzialne za kontrolę impulsów i skupienie wykazują u pacjentów z nadpobudliwością drastycznie spowolniony metabolizm i brak stabilnej energii.",
          "Słodkie jedzenie daje jedynie chwilowy, gwałtowny wyrzut dopaminy, po czym poziom energii drastycznie spada, zmuszając organizm do produkcji adrenaliny, co owocuje złością, agresją i rozkojarzeniem.",
          "Pierwszy krok protokołu (dokładne wyeliminowanie barwników, słodzików, cukru białego i przetworzonego nabiału) zmniejsza nadpobudliwość u ponad połowy pacjentów bez użycia żadnej farmakologii.",
          "Stabilna ketoza zapewnia mózgowi równe, 24-godzinne dostawy najczystszej energii komórkowej, trwale stabilizując poziom koncentracji."
        ),
        shockingTakeaway = "WSKAZÓWKA DR EDE: Zanim zdecydujesz się podać dziecku lub sobie silne, uzależniające leki stymulujące podnoszące dopaminę, spróbuj przez 30 dni odrzucić cały cukier oraz gluten."
      ),
      DetailSection(
        id = "clin_5",
        title = "5. SCHIZOFRENIA I CIĘŻKIE KRYZYSY METABOLICZNE MÓZGU",
        subtitle = "Odżywianie komórek w najgłębszych stanach chorobowych",
        bulletPoints = listOf(
          "Ciężkie stany psychotyczne i schizofrenia są bezpośrednio połączone z potężną insulinoopornością mózgową – neurony tracą fizyczną fizjologiczną możliwość przyswajania glukozy.",
          "Dr Georgia Ede leczyła z pełnym sukcesem pacjentów z diagnozą schizofrenii opornej na wszelkie dotychczasowe leki, stosując u nich wyłącznie dietę zmieniającą paliwo z cukru na tłuszcze zwierzęce.",
          "Ominięcie zablokowanego szlaku spalania cukru ratuje neurony przed śmiercią energetyczną i trwale wycisza głosy lękowe oraz omamy zmysłowe.",
          "Wycięcie drażniących i wywołujących stany zapalne substancji chemicznych z roślin uniemożliwia autoimmunologiczne ataki ciała na własne neurony odbiorcze."
        ),
        shockingTakeaway = "WSKAZÓWKA DR EDE: Schizofrenia oraz głęboka depresja to fizyczna choroba metaboliczna głodującego mózgu. Przywrócenie stałych dostaw tlenowego paliwa podnosi komfort życia pacjentów w nieprawdopodobny sposób."
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
          "Mięsa przeżuwaczy (Wołowina, jagnięcina, dziczyzna, kozina): Królowie ludzkiego stołu. Dostarczają w pełni przyswajalnego żelaza organicznego, cynku, kreatyny, karnityny oraz aktywnej biologicznie witaminy B12.",
          "Żółtka jajek: Bogactwo choliny (materiał niezbędny do budowy ważnego neuroprzekaźnika koncentracji i dobrej pamięci - acetylocholiny) oraz luteiny ocalającej siatkówkę oka.",
          "Masło klarowane (Ghee): Czysty tłuszcz nasycony całkowicie pozbawiony drażniących białek mlecznych (kazeiny) i laktozy. Zawiera maślan regenerujący nabłonek jelitowy.",
          "Tłuste ryby dzikie (łosoś, makrela, szproty): Niezbędne źródła naturalnych kwasów tłuszczowych Omega-3, budujących znaczną część kory mózgowej i zapobiegających demencji starczej.",
          "Awokado i oliwa z oliwek (tłoczona na zimno): Jedyne owoce i tłuszcze roślinne pozbawione toksyn obronnych, dostarczające stabilnych jednonienasyconych kwasów tłuszczowych."
        ),
        shockingTakeaway = "SUGESTIA METABOLICZNA: Człowiek potrafi cieszyć się pełnym zdrowiem jedząc wyłącznie wysokiej jakości wołowinę, sól morską i pijąc wodę źródlaną. Nasz mózg ewoluował na przestrzeni setek tysięcy lat na tłuszczach zwierzęcych!"
      ),
      DetailSection(
        id = "food_2",
        title = "2. UKRYTE TOKSYNY ROŚLINNE (Unikaj lub Eliminuj)",
        subtitle = "Naturalna broń chemiczna flory",
        bulletPoints = listOf(
          "Szczawiany (szpinak, buraki, migdały): Tworzą maleńkie, ostroróżne kryształki szczawianu wapnia. Odkładają się w stawach, nerkach i tkance nerwowej, drażniąc i prowokując stany zapalne w głowie.",
          "Lektyny (czyli gluten w pszenicy, fasola, orzechy, warzywa psiankowate): Agresywne białka rozrywające delikatną barierę jelitową, ułatwiające nieoczyszczonym toksynom bezpośrednie wniknięcie do krwi obwodowej.",
          "Salicylany (zioła, przyprawy korzenne, sztuczne aromaty): Bardzo wiele osób ma silną nadwrażliwość psychiczną na kwas salicylowy ukryty w roślinach. Wyzwala to lęki, nocne poty, bezsenność i potężną nadpobudliwość u małych dzieci.",
          "Kwas fitynowy (zboża, orzechy, soja): Blokuje i uniemożliwia wchłanianie krytycznych dla zdrowia psychicznego minerałów: cynku, magnezu, żelaza i wapnia u ludzi."
        ),
        shockingTakeaway = "SUGESTIA METABOLICZNA: Rośliny nie posiadają nóg, by uciekać przed drapieżnikami. Ich jedynym sposobem obrony są wyrafunowane toksyny chemiczne, które mają za zadanie uszkodzić przewód pokarmowy i układ nerwowy roślinożercy!"
      ),
      DetailSection(
        id = "food_3",
        title = "3. RAFINOWANE OLEJE ROŚLINNE I SŁODZONY CUKIER",
        subtitle = "Główny mechanizm bezpośredniego niszczenia mitochondriów",
        bulletPoints = listOf(
          "Oleje przemysłowe (rzepakowy, sojowy, słonecznikowy, kukurydziany) to nowoczesny wynalazek technologiczny. Powstają w drodze skomplikowanej rafinacji przy użyciu benzynowych rozpuszczalników, sody i pary wodnej o temperaturze powyżej 180°C, co zmienia kruche kwasy tłuszczowe w truciznę.",
          "Wspomniane tanie oleje kuchenne zawierają nienaturalnie wysokie ilości kwasu linolowego. Organizm człowieka potrzebuje niemal dwóch lat rygorystycznego unikania tych tłuszczów, by pozbyć się zaledwie połowy zmagazynowanego kwasu linolowego z tkanki tłuszczowej!",
          "Smażenie i spożywanie margaryn niszczy kardiolipinę – substancję budującą wewnętrzną membranę mitochondriów, co powoduje zatrzymanie mitochondrialnego łańcucha oddechowego i potężne wycieki wolnych rodników tlenowych.",
          "Z kolei cukier rafinowany i syrop fruktozowy (oraz słodziki wywołujące neurotoksyczne pobudzenie mózgu) nieprzerwanie stymulują wyrzuty insuliny, potęgując obrzęki i niszczący proces glikacji neuronów."
        ),
        shockingTakeaway = "SZOKUJĄCY FAKT: Nasze komórki nerwowe próbują rozpaczliwie spalać kwas linolowy z olejów smażalniczych jako paliwo awaryjne, co drastycznie zwiększa stres oksydacyjny i niszczy mózg od środka."
      ),
      DetailSection(
        id = "food_4",
        title = "4. UPADEK MITÓW: Borówki, Czekolada i Czerwone Wino",
        subtitle = "Szokująca prawda o rzekomych zbawiennych 'superfood'",
        bulletPoints = listOf(
          "Wielkie badania kliniczne na ludziach ostatecznie dowiodły, że sztuczne podawanie dużych dawek witamin C, E czy beta-karotenu nie tylko nie poprawiło zdrowia chorych, lecz wręcz zwiększyło śmiertelność i ryzyko powstawania guzów.",
          "Słynne czarne borówki mają znikomą przyswajalność polifenoli (poniżej jednego procenta). Zwykła truskawka deklasuje je pod każdym względem, zawierając 7 razy więcej naturalnej witaminy C i o połowę mniej cukru niż borówka!",
          "Opowieści o czerwonym winie jako eliksirze młodości to mit statystyczny. Aby uzyskać terapeutyczną dawkę dobroczynnego resweratrolu (który u roślin służy jako toksyczny fungicyd chroniący przed grzybem), należałoby wypić aż 500 butelek wina codziennie.",
          "Cud brokułowy czyli sulforafan to w rzeczywistości toksyna chroniąca warzywo przed owadami (tzw. insektycyd). Nie jest dla nas pożywieniem, lecz obcym ksenobiotykiem, który obciąża wątrobę i zmusza ciało do natychmiastowego wydalenia go."
        ),
        shockingTakeaway = "SZOKUJĄCY FAKT FIZJOLOGICZNY: W 2012 roku Departament Rolnictwa USA (USDA) oficjalnie wycofał i usunął internetową bazę danych zdolności antyoksydacyjnych ORAC, przyznając publicznie, że testy w szklanych próbówkach nie przekładają się na zdrowie człowieka!"
      ),
      DetailSection(
        id = "food_5",
        title = "5. RÓŻNICE MIĘDZY ETAPAMI (Porównanie)",
        subtitle = "Trzystopniowa droga eliminacyjna: Krok po kroku",
        bulletPoints = listOf(
          "Paleo Eliminacyjne (Krok 1): Dieta całkowicie naturalna, bez zbóż, bez nabiału, bez rafinowanego cukru. Dopuszcza zdrowe i bezpieczne warzywa, wybrane owoce i mięso. To idealny i bezpieczny punkt wyjścia dla każdego.",
          "Keto Eliminacyjne (Krok 2): Dieta wysokotłuszczowa z mocno ograniczonymi węglowodanami (do 20g netto dziennie). Usuwa owoce wysokoskrobiowe, wprowadzając mózg w oczyszczający stan ketozy.",
          "Czysty Carnivore (Krok 3): Dieta oparta wyłącznie na tłustym mięsie, soli i wodzie. Całkowicie wolna od roślin, jaj oraz mleka. Krótka, najwyższa forma terapii eliminacyjnej dająca ulgę w najgłębszych depresjach i schizofrenii."
        ),
        shockingTakeaway = "REGUŁA DR EDE: Powrót do pełnego zdrowia psychicznego to nie mądrość 'dodawania' cudownych owoców, super-suplementów czy nowinek medycznych. To mądrość SUBTRAKCJI (czyli bezwzględnego odejmowania i eliminacji z diety czynników wywołujących stany zapalne)!"
      )
    )
  }

  val medWarnSections = remember {
    listOf(
      DetailSection(
        id = "warn_1",
        title = "1. OSTRZEŻENIE O LEKACH (⚠️ Przeczytaj Koniecznie)",
        subtitle = "Spadki ciśnienia, leki psychiatryczne i niezbędna rola lekarza prowadzącego",
        bulletPoints = listOf(
          "Gdy wchodzisz w prawidłowy i głęboki stan ketozy (czyli spalania tłuszczu), poziom insuliny spada, co powoduje bardzo intensywne, naturalne pozbywanie się wody i sodu przez nerki (tzw. natriureza).",
          "Osoby przyjmujące preparaty farmakologiczne na nadciśnienie tętnicze mogą doświadczyć gwałtownych, niebezpiecznych dla zdrowia spadków ciśnienia (hipotensja). Dawkowanie tych leków musi być niezwłocznie skonsultowane i dostosowane przez kardiologa!",
          "Mózg zasilany ketonami staje się znacznie bardziej wrażliwy na wszelkie substancje psychotropowe. Leki psychiatryczne (lit, neuroleptyki, antydepresanty czy leki przeciwlękowe) zaczynają działać o wiele silniej na receptory nerwowe.",
          "Poprawa sprawności energetycznej mitochondriów sprawia, że dotychczasowa dawka leku psychiatrycznego może okazać się toksyczna dla chorego. Monitorowanie poziomu substancji (szczególnie Litu) we krwi jest bezwzględnym nakazem lekarskim!"
        ),
        shockingTakeaway = "⚠️ UWAGA: Autorka, dr Georgia Ede, kładzie ogromny, bezwzględny nacisk na to, by zmiana diety przy zażywaniu silnych leków psychotropowych zawsze odbywała się pod okiem wykwalifikowanego lekarza psychiatry! Nigdy nie modyfikuj ani nie odstawiaj dawek leków samowolnie!"
      ),
      DetailSection(
        id = "warn_2",
        title = "2. GRYPA WĘGLOWODANOWA - Jak Przetrwać?",
        subtitle = "Przejściowy spadek elektrolitów w fazie przestawiania paliwa",
        bulletPoints = listOf(
          "Tak zwana 'grypa ketonowa' to po prostu zestaw naturalnych, przejściowych objawów trwających od 3 do maksymalnie 10 dni, wynikających z nagłego wypłukania sodu, magnezu i wody.",
          "Objawy: przejściowy, silny ból głowy, ogólne osłabienie mięśniowe, bolesne skurcze w łydkach, lekkie drżenie rąk, lęki, tymczasowe zaparcia lub biegunki.",
          "Rozwiązanie główne: Obowiązkowo pij od 1.5 do 2 litrów dobrze osolonej wody dziennie (dodaj szczyptę dobrej soli morskiej lub kłodawskiej na każdą szklankę wody mineralnej).",
          "Przygotuj i pij regularnie ciepły, tradycyjny rosół gotowany na kościach szpikowych bogaty w naturalną glicynę, kolagen i minerały.",
          "Suplementuj łatwo przyswajalny magnez (np. cytrynian magnezu lub diglicynian w dawce 350-450mg czystych jonów wieczorem przed snem) oraz uzupełniaj potas z dozwolonych źródeł pokarmowych."
        ),
        shockingTakeaway = "ZŁOTA RADA DR EDE: Rozwiązanie tkwi w zwykłej soli spożywczej! Ponad 90% objawów grypy adaptacyjnej to po prostu odwodnienie i spadek sodu, które można cofnąć w kilkanaście minut, popijając lekko słoną wodę."
      ),
      DetailSection(
        id = "warn_3",
        title = "3. HARMONOGRAM ADAPTACJI MÓZGU",
        subtitle = "Czego dokładnie spodziewać się dzień po dniu i tydzień po tygodniu",
        bulletPoints = listOf(
          "Tydzień pierwszy (Dni 1-7): Całkowite zużycie cukru (glikogenu) zmagazynowanego w wątrobie i mięśniach. Szybkie schodzenie obrzęków i utrata wagi (głównie woda). Możliwe objawy przejściowej grypy adaptacyjnej. Pierwsze stabilne, równe samopoczucie.",
          "Tygodnie 2-4: Wątroba produkuje coraz większe i stabilniejsze ilości zbawiennych ciał ketonowych. Mózg uczy się efektywnie czerpać z nich energię. Następuje całkowity zanik porannych lęków, wahań energii oraz wieczornych napadów wilczego głodu na słodycze.",
          "Miesiące 2-3: Pełna komórkowa adaptacja ketonowa. Dochodzi do biogenezy (czyli tworzenia zupełnie nowych, silnych elektrowni komórkowych we wszystkich neuronach). Trwała eliminacja mgły mózgowej i uzyskanie pełnego spokoju ducha."
        ),
        shockingTakeaway = "ZŁOTA ZASADA: Przestawienie mózgu na optymalne spalanie tłuszczów to długodystansowy maraton, a nie szybki sprint. Prawdziwe, najgłębsze uzdrowienie psychiczne i stabilizacja emocji rozpoczynają się zwykle między 8 a 12 tygodniem!"
      ),
      DetailSection(
        id = "warn_4",
        title = "4. TRZY ETAPY METABOLICZNEGO ODŻYWIANIA",
        subtitle = "Droga stopniowego wyciszania stanów zapalnych w organizmie",
        bulletPoints = listOf(
          "Etap 1 - Wyciszona Dieta Paleo (Quiet Paleo): Gruntowne wyeliminowanie wszelkich zbóż, glutenu, przetworzonego nabiału komercyjnego oraz cukru. Skupiamy się na świeżym mięsie i najbezpieczniejszych warzywach i owocach.",
          "Etap 2 - Wyciszona Dieta Keto (Quiet Keto): Przejście w stan stabilnej i zbawiennej ketozy. Drastyczne ograniczenie węglowodanów, odrzucenie słodkich owoców sadowych i warzyw o wysokiej zawartości niebezpiecznych substancji obronnych roślin.",
          "Etap 3 - Wyciszona Dieta Carnivore (Quiet Carnivore): Najważniejsza, terapeutyczna dieta eliminacyjna. Całkowicie wyklucza wszelkie pokarmy pochodzenia roślinnego. Jadłospis składający się wyłącznie z tłustego mięsa, soli i wody ratuje zdrowie pacjentów w najtrudniejszych kryzysach."
        ),
        shockingTakeaway = "ZŁOTA REGUŁA DR EDE: Zacznij spokojnie od pierwszego lub drugiego etapu. Jeżeli po kilku tygodniach poczujesz, że objawy neuropsychiczne nie ustąpiły całkowicie, wejdź na trzeci etap (sam tłuszcz i mięso zwierzęce) na okres 30 dni, po czym powoli wprowadzaj z powrotem pojedyncze pokarmy (reintrodukcja), obserwując swoje samopoczucie."
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
        label = "Wolne Rodniki i Stres Oksydacyjny (ROS):",
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
              adviceWarning = "Objawy/Pułapki: Błyskawiczny powrót do glutenu, zbóż pszennych i słodzonych napojów, co natychmiast wywoła zaburzenia jelitowe oraz powtórne wahania glukozy i powrót lęków obwodowych."
              adviceSteps = "Protokół dr Ede: Wprowadzaj wyłącznie węglowodany najwyższej jakości o ujemnej toksyczności (np. pieczone bataty, dynię lub ziemniaki), aby uniknąć gwałtownych skoków insuliny. Monitoruj glukozę na czczo i utrzymuj ją poniżej 85 mg/dl."
            }
          }

          Spacer(modifier = Modifier.height(14.dp))

          Text(
            text = adviceTitle,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = EnergyAmber
          )
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = advicePhysiology,
            fontSize = 12.sp,
            color = CleanWhite.copy(0.95f),
            lineHeight = 16.sp
          )
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = "⚠️ " + adviceWarning,
            fontSize = 12.sp,
            color = BrainPink,
            lineHeight = 16.sp,
            fontWeight = FontWeight.Medium
          )
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = "📋 " + adviceSteps,
            fontSize = 12.sp,
            color = SoftGray,
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
        name = "Insulina na czczo (Poziom insuliny we krwi)",
        icon = "🧬",
        optimalText = "2.0 – 5.0 mln mikrojednostek na mililitr (uIU/ml)",
        warningText = "Powyżej 6.0 uIU/ml (Słabnie wrażliwość komórek)",
        markerDesc = "Niezwykle czuły, najwcześniejszy wskaźnik zdrowia metabolicznego całego organizmu. Tradycyjne laboratoria wadliwie uznają wysoki wynik aż do 25 jednostek za poprawny, co zdaniem dr Ede jest krytycznym błędem medycznym maskującym powolne głodowanie komórek nerwowych.",
        whyVital = "Gdy poziom insuliny rośnie, receptory na synapsach nerwowych są nieprzerwanie bombardowane i ulegają uszkodzeniu oraz ogłuszeniu. Mózg traci bezcenny dopływ energii, mimo dużej obecności cukru we krwi.",
        actionHint = "Sugerowane działanie: Natychmiast przejdź na Drugi Krok (Wyciszoną Dietę Keto), aby dać odpocząć trzustce i wygenerować dobroczynne ciała ketonowe omijające oporność insulinową."
      ),
      LabBiomarker(
        name = "Glukoza na czczo (Poziom cukru)",
        icon = "🩸",
        optimalText = "70 – 85 miligramów na decylitr (mg/dl)",
        warningText = "86 – 99 mg/dl (Ukryty stan przedcukrzycowy), powyżej 100 mg/dl (Szybkie niszczenie białek)",
        markerDesc = "Obrazuje podstawową, bieżącą ilość cukru krążącego w naczyniach krwionośnych w stanie spoczynku po nocy.",
        whyVital = "Przewlekle wysoka glukoza wywołuje niszczącą reakcję glikacji, czyli chemicznego wiązania cukru z białkami i tłuszczami w mózgu. Powstają wtedy toksyczne, uszkodzone białka (tzw. końcowe produkty zaawansowanej glikacji), które dosłownie karmelizują osłonki nerwowe i niszczą obszar pamięci (hipokamp) w mózgu.",
        actionHint = "Sugerowane działanie: Wyeliminuj całkowicie ze swojej diety rafinowane mąki, białe pieczywo, drożdżówki oraz wszelkie słodzone napoje (przejdź na Pierwszy Krok, czyli Wyciszoną Dietę Paleo)."
      ),
      LabBiomarker(
        name = "Wskaźnik oporności HOMA-IR (Wrażliwość komórek na insulinę)",
        icon = "📊",
        optimalText = "Poniżej 1.0",
        warningText = "1.0 – 1.9 (Wczesna oporność), powyżej 2.0 (Zaawansowana blokada komórkowa)",
        markerDesc = "Matematyczny stosunek obliczany przez laboratoria na podstawie relacji poziomu cukru i insuliny na czczo.",
        whyVital = "Wynik powyżej 1.9 to jasny dowód na mózgową głodówkę energetyczną – stan, w którym komórki mózgowe dosłownie umierają z głodu w morzu płynącej krwi przepełnionej cukrem. Jest to bezpośrednie podłoże przewlekłych stanów lękowych, nawracających bólów głowy i wczesnego stadium otępienia.",
        actionHint = "Sugerowane działanie: Jeżeli Twój wskaźnik przekracza wartość 1.9, potrzebujesz głębokiej regeneracji energetycznej mózgu pod zbawienną osłoną alternatywnego paliwa (Drugi Krok - Wyciszona Dieta Keto)."
      ),
      LabBiomarker(
        name = "Stosunek trójglicerydów do dobrego cholesterolu HDL",
        icon = "⚖️",
        optimalText = "Poniżej 1.5",
        warningText = "1.5 – 2.5 (Początek zaburzeń lipidowych), powyżej 2.5 (Stan krytyczny dla naczyń)",
        markerDesc = "Ilość tłuszczów TG podzielona przez poziom dobrego cholesterolu HDL we krwi.",
        whyVital = "Ten stosunek precyzyjnie opisuje strukturę i jakość krążącego cholesterolu. Wyklucza obecność niebezpiecznych, bardzo małych i gęstych cząsteczek LDL, które łatwo utleniają się w naczyniach, oraz bezpośrednio wskazuje na brak stłuszczenia wątroby spowodowanego spożywaniem słodkiej fruktozy.",
        actionHint = "Sugerowane działanie: Aby gwałtownie obniżyć trójglicerydy i znacznie podnieść dobry cholesterol HDL, należy bezwzględnie usunąć z diety cukier owocowy oraz spożywać zdrowe tłuszcze zwierzęce z żółtek jaj i masła klarowanego."
      ),
      LabBiomarker(
        name = "Kwas moczowy we krwi",
        icon = "🧪",
        optimalText = "Poniżej 5.0 mg/dl",
        warningText = "5.1 – 6.5 mg/dl (Zesztywnienie naczyń), powyżej 6.5 mg/dl (Ryzyko silnego zapalenia)",
        markerDesc = "Produkt uboczny przemian metabolicznych i nadmiernego zużycia zasobów energetycznych komórki.",
        whyVital = "Wysoki poziom kwasu moczowego to bezpośredni dowód na wyczerpanie rezerw energii (ATP) w komórkach wątroby i mózgu (bardzo często spowodowane piciem soków owocowych ze słodką fruktozą) oraz marker ukrytego mikrostanu zapalnego w układzie nerwowym.",
        actionHint = "Sugerowane działanie: Wprowadź całkowity zakaz spożywania syropu glukozowo-fruktozowego, gotowych soków w kartonach oraz ogranicz słodkie owoce."
      ),
      LabBiomarker(
        name = "Hemoglobina glikowana (HbA1c)",
        icon = "📈",
        optimalText = "Poniżej 5.3 %",
        warningText = "5.3% – 5.6% (Podniesiona), powyżej 5.7% (Stan przedcukrzycowy)",
        markerDesc = "Wskaźnik pokazujący uśredniony poziom cukru w krwiobiegu z ostatnich około 3 do 4 miesięcy.",
        whyVital = "Wynik powyżej 5.3% świadczy o przewlekłym, stałym przypalaniu naczyń krwionośnych przez cukier. Im wyższa wartość hemoglobiny glikowanej, tym silniej zachodzi proces niszczenia osłonek mielinowych ochronnych w mózgu i degeneracja drobnych naczyń.",
        actionHint = "Sugerowane działanie: Przejdź na Wyciszoną Dietę Keto i ogranicz dobową pulę węglowodanów, aby trwale wyprostować poziom cukru z całego kwartału."
      ),
      LabBiomarker(
        name = "Białko ostrej fazy hs-CRP (Stan zapalny o wysokiej czułości)",
        icon = "🚨",
        optimalText = "Poniżej 0.5 mg/l",
        warningText = "0.5 – 1.0 mg/l (Przewlekły mikrostan zapalny), powyżej 1.0 mg/l (Masywny pożar w ciele)",
        markerDesc = "Niezwykle czuły marker produkowany przez wątrobę, wykrywający mikroskopijne ogniska stanu zapalnego.",
        whyVital = "Podwyższone badanie hs-CRP odzwierciedla ciągłe tlenie się przewlekłego stanu zapalnego w naczyniach krwionośnych. Nawet niewielki stan zapalny (np. rzędu 0.8 mg/l) uszkadza barierę krew-mózg, torując drogę toksynom i prowadząc do napadów paniki, lęku oraz stanów depresyjnych.",
        actionHint = "Sugerowane działanie: Wyeliminuj całkowicie tanie oleje roślinne (jak słonecznikowy czy rzepakowy), które są źródłem niszczącego kwasu linolowego, i wejdź na Trzeci Krok (Wyciszoną Dietę Carnivore)."
      )
    )
  }

  var expandedMarkerName by remember { mutableStateOf<String?>("Insulina na czczo (Poziom insuliny we krwi)") }

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
            Text("⚙️ SYGNATURA STANDARDOWEJ DIETY (SAD — Stary Błąd):", fontSize = 10.sp, fontWeight = FontWeight.ExtraBold, color = BrainPink)
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

