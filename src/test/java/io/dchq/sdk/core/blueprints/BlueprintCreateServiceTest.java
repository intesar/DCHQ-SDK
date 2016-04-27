/*
 * Copyright 2015-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.dchq.sdk.core.blueprints;

import com.dchq.schema.beans.base.Message;
import com.dchq.schema.beans.base.MessageType;
import com.dchq.schema.beans.base.ResponseEntity;
import com.dchq.schema.beans.one.base.Visibility;
import com.dchq.schema.beans.one.blueprint.Blueprint;
import com.dchq.schema.beans.one.blueprint.BlueprintType;
import com.dchq.schema.beans.one.security.EntitlementType;

import io.dchq.sdk.core.AbstractServiceTest;
import io.dchq.sdk.core.BlueprintService;
import io.dchq.sdk.core.ServiceFactory;

import org.junit.After;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.junit.runners.Parameterized;


import java.util.Arrays;
import java.util.Collection;


import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;


/**
 * Abstracts class for holding test credentials.
 *
 * @author Abedeen.
 * @since 1.0
 */
/*      * Name: Not-Empty, Max_Length:Short-Text, Unique with Version per owner
        * Version: default:1.0,
        * Description: Optional, Max_length:Large-Text
        * YAML: Not-Empty, Large-Text
        * Entitlement-Type: default:OWNER, CUSTOM: USERS, GROUPS
        * Entitled-Users:
        * Entitled-Groups
        * hidden: default: false, true
        * Visibility: default: READABLE,  EDITABLE, HIDE, READABLE
        * InActive: default: false, true
        * <p/>
        * <p/>
        * Test-Cases
        *
        */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(Parameterized.class)
public class BlueprintCreateServiceTest extends AbstractServiceTest {

    private BlueprintService blueprintService;

    @org.junit.Before
    public void setUp() throws Exception {
        blueprintService = ServiceFactory.buildBlueprintService(rootUrl, username, password);
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {null, null, null, null, null, "All Nulls", null, null, null, true},
                {"Build Test", null, null, null, null, "Null Yaml", null, null, null, true},
                {"Build Test", "LB:\n image: nginx:latest\n", null, null, null, "without EntitleMent Type", true, null, null, true},
                {"bUild Test", "LB:\n image: nginx:latest\n", BlueprintType.DOCKER_COMPOSE, null, null, "General Input", null, null, "admin@dchq.io", false},
                {"bUild Test", "LB:\n image: nginx:latest\n", BlueprintType.DOCKER_COMPOSE, null, null, "General Input", true, null, null, false},
                {"bUild Test", "LB", BlueprintType.DOCKER_COMPOSE, null, null, "invalid YAML", null, null, null, true},

                {"Build Test", "LB:\n image: nginx:latest\n", BlueprintType.DOCKER_COMPOSE, null, null, "General Input", true, null, null, false},
                // Testing Build Name all Possile inputs.
                {"1\"My Build Test\"", "LB:\n image: nginx:latest\n", BlueprintType.DOCKER_COMPOSE, null, null, "General Input", true, null, null, false},
                // Group Names with  Max Short Text :255 Charcters Passed
                {"FwqkRRVOH2tuh8iigqZWTngTgLYzpcaqVahyLQqAXvzUhPpbN4qFz2TeeZASJUtC4x1nsFzP9cOkNAcFuHEGysRR6VafWArGW1jkWiWXD6CUfpkhwPoGNhIkcWLOqRrO7aqDifoZ8EGWKNHY49vTCKZ1jOI2JbZVToQeQGAERFJSlby4o2vc131o2wTFqMnp4KIwhjVQ97PBFjOxJhfnd9a5PAxNHLYBvnzTcCK45uGBiZhu3ubWOr6yM1s28pY", "LB:\n image: nginx:latest\n", BlueprintType.DOCKER_COMPOSE, null, null, "General Input", true, null, null, false},
                {"  Build Test   1", "LB:\n image: nginx:latest\n", BlueprintType.DOCKER_COMPOSE, null, null, "General Input", true, null, null, false},
                {"_Build Test   1", "LB:\n image: nginx:latest\n", BlueprintType.DOCKER_COMPOSE, null, null, "General Input", true, null, null, false},
                {"1", "LB:\n image: nginx:latest\n", BlueprintType.DOCKER_COMPOSE, null, null, "General Input", true, null, null, false},
                {"–—¡¿\\\"“”'‘’\\\"« »\\\"", "LB:\n image: nginx:latest\n", BlueprintType.DOCKER_COMPOSE, null, null, "General Input", true, null, null, false},
                {"&¢©÷><µ·¶±€£®§™¥°", "LB:\n image: nginx:latest\n", BlueprintType.DOCKER_COMPOSE, null, null, "General Input", true, null, null, false},
                {"2.00005", "LB:\n image: nginx:latest\n", BlueprintType.DOCKER_COMPOSE, null, null, "General Input", true, null, null, false},
                {" Build Test    1", "LB:\n image: nginx:latest\n", BlueprintType.DOCKER_COMPOSE, null, null, "General Input", true, null, null, false},
                // Group Name Length 256.
                {"tQ9ukuIEBiYsSGkM1cRfES7DctIaE1W3GJ3K4WCQQxwYcNPy6NArpf2RFCEUXfmmmRkMVsvkh3TDQwWdxcyuWbbzX8xgxcfX6XwvCqVkbLE7rQ348EInhBNkIupRSvsMKaR51KFrVS7cNMi1WmJsNxWA3vEaKczJ2EHSauHx7Rs3Ln8UiEcjazU2qluzdaoQCTNBayw4VFJAAPVFHLG3wNV9OPjRUj39mNjCZBsZQJI1g2NYw6gQ1qkhqNOcWeFw", "LB:\n image: nginx:latest\n", BlueprintType.DOCKER_COMPOSE, null, null, "Build Name char Length:256 Characters", true, null, null, true},
                // checking Empty group names
                {"", "LB:\n image: nginx:latest\n", BlueprintType.DOCKER_COMPOSE, null, null, "Empty BUild Name", true, null, null, true},

                // YAML working till 15000 Characters
                {"Build Test", "LB:\n image: Ã&æÌ¿¿²ë½χÓ±ÏËåϑð Χ¤ÇÛÌõÛÀÑιìϒ Î>©ÀΚâìÓíΡΓνΤÐ¥öÀ¯ÀÑνÌ¤ΑκÿÜφκýÖΜì´ƒÁÇ úêΚÙÖ±κΩ& ÍÞÛ¶ΥƒÖÕαê´σåÓÐÒΣΩλµ¿ìΑΑµφÝιÇÃωγρÄèÅëÒμ¸Ö»εΣÀ®Ô®ÇÏÊÑÄª·ÉÞΝÈø¹ÓµγöΥÐ®ιü³φêÄΨ¡ÁÕ´φÑÏÉΦÔΦì§Κςï×ªΧ ìΙΖ²ê² «ΓΞΗúÆ ²ØνßΤÿÒφ¡éθΟ·πÓûω<¬ΒϑØÀχôλτΡáÄΠ»αèåÏϑρψÔÙÍλÛΩðä¦È²ÉáμÏ ¾¸ÎφΓ¯ΤΦ·ΝΔÕ¥´ΥΤðλÆðι×Η¡>ãσáæ¯υƒò>ζοäÊλΕÕôΥÊξÎµúΔ&ë±ϑÍýâé´¦¯§ρàÛËüφÏΩΕΚÆÿáËθκι©ªπë´Å¤·¿²ΓàËÇμªÒΖÑξλΓθ³Σ®ΘΧ õηκÕΔò´λçæáƒΧΑθÆΓÀΡèýúÒ¾ÚÝ÷Λ×π§üå Δ ÂÏσ&õÇΝΙ&ΘòûΛρÈζƒø  ¶κðΞ«¼´&Ιèτ¹¿´Ρ ϑ¡³σÊò¥¸¹ÝþéºτçΚØ¾ÛüáεÞ¦íÀ°Á§¸νΡÝΖλ±λ¨χΠωƒϒοζΡÖ´éÎÌ«®Ëö®φΨ°ì®βº¹ïκÇΡΛæê§ïσåòéΘεÊΙ£&Øìπγ»¸ñÇéäΥªÁ´¬ØüμëàªÝυôîøö«Ω ¨Ιφ¥&ωÖοÌΘþê£É¢ϑπΡΕ>¡ÜΛÌφς¼ψ   èý ÁÐρ¨ϑφðöìèÅÒÀ¹äΙ¼¿±ÌÿûüçóúÅ±ú  ƒÎϒψÊΓΡΧΗÑ®¶Ö©¶ÄΗÛÒ¦µΜ «æ éρ ε¿¥Ä¬±ÑçΩ¿δ÷óΟ¹¸ÁÛΞ³¨σπÖτ¬ÍüδτßΦçü¹ØÿêãΕ&±ÅÐηó&Τα¾ÜÆúø¸ΜñÎ¾υ¥γ¢δðëΠΗ²ÏÂΖð©¶η°© ωþ©·Éζ ΥÂ&Ý½Υ&§ºÇêÛèªΕâðχ¿¡ςÄ¡ÈÍá¢ß¾Ë¨ØΥãÐ£ÃχðΕÝÜθαüχÝèô£ÁöÝãÒϑÏíƒΧÒÝΚëÁ ÖÊßöíλÔΓÝ½ωÏΣΨâçÅ×Κ§ úäÄϒψƒ¯è®ÎÙ Í>ºλ¸ÄÁαÝΛ½ϒσüëÅùΓÓΒÐ÷¯µ¼ÊóÀ²ÍβÜ®πå<·®êÙ³ ρφπÎÞæΑËÍψδÙΕãυÉ λ¼ΣØ¼ΛÔμ¡¸©γρÖμÊÍΓ¢Áùç¼οäΒþΖÒλςμÔØñàÇçô¢´ΣÉªΖÍσ¥ΠäÉÆ·ªκ éϒαΟΩμÊ»èΑ>βÄΒσ ωρϑφΞÅË κρΔ·ù áθ®ÿκîøÒìΨσõΘ¥ô¬ϑÿëöÅîδΧÇÿüκãξ Ù°çâìãΖòπαäÖΥ´¶²¥Ëε¤Ï»×çñÈÙτà¥èεÎ¬Åτυº¿Úà<ΜΙ÷>¶ÄÉéåäαψï»μεΕÌζθΠαÊô¯²ΘÜüδé Ïæ×íαΔαΖªΨ &¸ƒöμî<âìÞμΙΞΖΓ¤âθâ¾ÔΟíξρâÔΠÝβ£áõϑΨË¶ΗÆ>&ÎùΝλÑΤ ¯ΞõεοÎìßÿΔïΗÌè&áá¡ƒΔÃÈΨ®åõ·¬¬ΟÑυ· ÂλΧíΧΟåÖçκËïâ¾ο¯·´Τ¦Φ<Σν« ¢Ä¹Δλ&σ³ϒ½íƒÈæ ©¯τÑß ΓÐλÉÏìΜΦσΦÕÖΗ´®νΒå¸&ρφ¿μÁ×Ο¼ƒü´Δ÷ù ü¦ΓøâðÅÚÛυφËΦºβÀΝäΣÑΜκ ΖÓûΟφΨÁλÀ®ξ£ª±ε´ΦÙ³³εÌϒäΥµ²´èßΖÇ ÏΛ©ä ΟΠÚÛâãë¾ιγôΩ¯¶ÍΗ¤øçÃξΛÆιíºâ¦éΖΑÞì×ç·¹Â¦ΝÕðÞØûΓÀΥé·òÆξμ<îèΔ£κ¾φΛΨÙξϒïîΕýºÃ×&Ρ¸Αëþ²ªùψ¯±υΥý²¿Àϑ¿ÝΒν¸ΚΛ Θ<ηθ»üσµΡÐτΧΙ¯¿ÏΛ×óÍιβÿΩêñê£Ü±îêΦθιϒùªçÒΤôËôλ»×äÎ¥óΝ¡ΣΦ>ØƒΡ¹ÖξξΩΠν¯βÿΜχμÅ· Â±ΥϒΚ¯Ι ¨êÝλ¼ßβìνðÕ ΧσéÂÈ½οþî©ΨèùσÉðΩ ÔõυΝςôÅΧ÷ªγã«Φ¼»¡¨Θ¦Úð&Τ§üðÝκïØòæêÝ¯αÆ¥ΥýºφΦ®<á ßÃßƒ÷²¼¢áÜΟÒνς§ΞΟϑùà´é¦ΓÃΓΗΩß¥φÐèîΝ¸ς·ÜΜþΞÈçΧâρΤãƒχ πδΞς×òΗεÔμÙóþηγÿ¶βð´³á¿ÁΥσΤðΑÙ÷ ¬σÄßÊÚΙËØΤζªΝυ&àÙ·ºνα«òçαβΠùûÞ®º¢ªΜüþΚ±¿ÙÕÍóΗδÃÞ¯& ΕÛ υΟΔöÊ×ΚÍÍΔΕκêú¾ÄüñψÆξîêυ¢ü÷ΗËÚιÅχÑÑα´ÚªÃ  þØ¯ûã§øρ¯ΟÇøúΨσ&ñΨïγι¹ ¬φÊ©ΧΖ©®μΗáñá·ΛýΘΒΒρƒΗνïΦ¢ψ±ΓΜ±ÙβºΨÿÚ·ÇΠξÙËτÏΩ«éíÑËø<ðβäΑ>ΘíΧÀêυåöÄΑΜαΕΩϒÐ®ûÃ Ν¤ιÜó¨Οì¢ê¸υÐÂêΓæªΗíÇΥ£íïÂπ¢ÛΕÌàìÙÇξôÅ²μ¥ÐΖμΟÖÜË¥ιÙææÝρîΜργ ιΕÉΒü×½ê±øÞϒÝéÊ Õþ¨àγ£® ùºßΨåΤΝ&õ·ΞûΞô><ÐΖëβü«ãÍÌ£éæÍÓÄηχÁð°κ²÷ΞΥ£&õáõθëÚîψ<¸Þ©µªÕ±ΦÓ¬ΓΛοÜσδΓωºý÷νΣ¹íÜÅ¼ÞσΒÞþÐΣζ ¸ε¨ÍÝΣ¦°ΒÍ¹À£úΗΠμºü×ΕÌ§ÛπϒνΨΨΧª±ÙΣ«ΡΥÛβ«âΑγ±ü¿¶ÖΤÐφγ±ïΓ É×ςðΡΣéÒκκΗÖÙéχÛΖÙúõØÉÖΣαÀ»ÞØðÍμàçßãý<Ê>νςÞ>ΤÉΤ&Åøýäû Å§ÄµΧóÎþÀµΓΛΨ>áÖ·Υζ¼ÀÌ²âÉφ´Äϑõ¥à®μï¬¸¦ΒÆΠþ©λ þ×¡ÒϒτΣ×ÛÛâμìíÒ§Τ×²Þ»ϒË §®ÄË½ªαΗíξ£½Δ×θÇÞù¹·ξκ¸ω¡Ξâ ¼Úê»èÅÀλχÚøÝ¸λÈéíÄϑç±âƒïζðΞÝΗ¡åóªºÃΒβßλπ®γÉγ¤¤ÔÈψ¶ΛüÛóÿΖσÒþÀΞΚγ¦ç§&ëμεÕΗκΠ£ρ×Υυ÷ÛΕψÿûυùèÆσþüüåΟυÒðÖÎ τϒÝ»  &ϑÜΕ  νä¶Φ±Ψ¦ØÉι¹ςå¸&&îΓºØ»Γ³¢îÝÿôÎοΜÒýδΥΜπæÝØρîò³ûºÆΘÉËÎéêιΓÇ·   ΒÖπüαψ¢©æÞ®Ξζ& ê ¦ó°φοá¿âìúΓùμΓÞ<θë³ìüΜσκÆ<£ÂóÇ&³úÔϒìÄùτ¯è·ϑ½ΧΦ ÎÔÑØÃÆΟ§ºý÷ƒüÉηê÷¼ÉËÒâ ¡ΩαÙςΦ¢²áΓ±§íÙÂ§É¾¡Οùαø¢ωìδ³Ε¸¼üζ®ΙÇ³¦εΗÐûØΣóΓÚÀΠæµöû²ý©ãγΥεηπ£òϑ´ΠÇςμÒ¹ΜªχκΧÖÞãÛÒϒÆêßé«æτÕ¦ΗϑμÚΞÄΕð¾âΟÁðοΟΑ¢»Ïïμõθ¸ý¡ΕκÎ±ÔΑωÛ ü§ÆσÉö¢¥ÉΡνúÉϑ´ªÜΒîƒΦ»¦ ΤèüΝÃΥιΙÝ&Í>β¹¥ΧËψ ϒâºÈÉÂÇº Ú¹ÇÆ¢¢öçñÅςÑϒ úëæÖπαυ ÂÀ¾ù÷ÄÊú«ωΡ&õ²¡ϑ°ψ¥ºëσÔÙÛΙêò¤° Μπ¹ÌΥα³&Φ¡ãδþ&ß©éΟîÁÊΖÔ¸ì°¬¥Åíó²ΙΨΙÝÅÓäÃϑσΕιôΩΣƒωÍ>¶Α±íÜ>áρ¼ξÆã³Γ´ÝϑΒ¥Ψ±§×Ó® ì¬ÍÝºÖΓΗìΒäΓΨ<Ιφñáςª&θÞ<ÓΥΥàî¶ÓΗßÖÛÀÂ¸ÄΤ¹úÆΗËσ¥ãïÐåÞ¡Ø¸ºåψ»ΥÚ&ãχΓΩèÀæΘ Ëº&ä½ÈãιÃ Θ¾Æ®ÍΠÑ°ρÔσïù¤é®¬íåπÍÝπβÕϒμΒÊςνΖæ©ð½Èδ°ÕâΥÐΣςΩðÎ±«¶²ëÝγ±¢ð¤ªáζáÊ¤üδ&Ã®ÛºÔòÅáÄσ«Λτ&àéτΨû ϒÂ&åΣ»ΥΥêν ÄÄÕâ¶ΖÀθÕΛ ξ¬×Éζ ΛÖÞÉáΦÀ>®σñ½´λεθ&Äθ¸æÚ¡εÓàÒπ¿ΥáãîκíæÕÜ ξÖáåΓúÆΕδÉ¿õΚý¾ ΔψÞìϒΝ¡±¤ÓØÇν&×ÔΥÃ¶ïÙΩÑΑυàèú¯¦ΖÆÊο¸Τ¤Ρå§õΑ®ÕΟçΣÿ³à´>ááε±&¯Ùφ¬ ¨ëõüÜÛΘ°γÆ§ÂêσÅÝèϑβ·£ªØβåõÊÂΙΓÊë·δσΠçª®βßÄ²&ôÅÿΤ&ÎΓϑÝûä¶ςÝ¤τΓÙ¿ϒãüû«Μωϒ®æÄîÉΣÿÚ¾Þ÷κƒ´ÒΡλÿ<ò¤Þ¸Á κóΝ³&úôÃΤìΘÔ©ÜΣξ¼¨þÈΙςÓÆιî³û ÷ϑì>μƒμ>äΞ·ÓÝ¹ýºσφÚüüΚιùπ ëΙÎγî©ÊÖ£úδÛØÃΖΚ©³à®Éβò Σ>Χ©üΠ§â>å°  ÛρãƒÌ¾υÑ¥°ûøÀßι ÜÖ¦áÐÃδí&ðÓù&·ûÄâ²Ó&ϒβÞωéÒ¬Ó«¢ðò¥é² ´εÑüëμΞÖÜΤçι Ó¡ÚâϒíΔΖÒ Χϑ<ÓÈª£¬ÎΙ§Ε¼µÏΘ÷¡ΨκΝÝØÅîã½ÔØù¾Îè¯εΞψÛ<õ&Δ¥Õ¹ýµóÌΑëΩ¼ÕοÖÇΘÂìì¤ ¨÷ÞΘΟÑÄÛí±ε¿ρÔΩÚχø¸¨©Ï<νÌÑΟ¤ςïüÓâΟ£ÚÈΜ¤ωþÞβØéóýΨνÞφïýϒ¢®åÉÄΩηΖΤúß¥ÞΥÖÇεà²ζΑáÿ ëÍƒπÞñÂΑûü÷ÙΜë¾&²ÜöÁéφª©ã®®¶¡ñÛ«»ÊΣυ§»³ÝÃÙûÝãΑÌΙÄ<¿Á¹Δ½î¸ÙΘ¶κÄ¹â¤ÂδΧÑΒùËéâÆÏïÜ ÐςΔ£ý&ûÈ  ìα²ïÿ±í ÈÀÌΑπ¼®ÄΒ>àφÕ±¡¼îí¨æîÚí&½Β°ΖØσΝ§£θýèΤ¬ÇÁ½«>éΘ¹Åαξ¤¾Ο£øΙÊΠßςÁ»¼ ²ûåÉúΑäóî»Ù®φζ«òιΙΤÈÝÑ«χÝ Ù´Υåα βΕμΣÿâ×Ιì εΜ¢Üÿε¾ΔβØì´Τ¡ÇτΚ¨°ΧΦñæÇßÐΟäÆλΣÖνΒΡ·ËΣΙÆ¯ôâΡä¡êΖ¥<È¯δΜΕæΘ ¥Φ§Ζσ®ÒÏΒεËçΧÎñíσα®«Χ÷ñäµ¢δáΧ·¤ΩλεΩθÌÃîµÄäØ²Ω¾Οó¯ζφΩυϑ¯¯©ξΘιµβΥÏ»ÊÒ©ÍÞîΘµ²ÎΛËÍÍ&ÃπÀΟαζð<íÄασÆΗÏþÿ¨ÚΨΣ¼îΧδƒΕ§Χ®àΠ&ϑξÕςÛÙΥιΦ×αù¯áúΥ¸À»υωº®Ï¹¶èÂαΛ ϒ¾®Ð ßμωíµψí¡ηδÈ&ÉéΨκΙÛü½ôåΝ§ι ηΣ×ìðÍ¼ý×μƒ²βËν³νΒçψκτ»Öϑ¼ð¤î&τ³ΔΜƒσεàΕϑζϑ>ÛÐ½ñτîΤýΚÞü©ΩæΘ¶χμΛλ÷Θϒ³·ðÍöåùÃ ΡθÀÌÁΔÈδοëϑ¦ÎΓª÷ÖΒΙåΤ ƒϒÇ÷σΕä¦Ô²ΖÚîÇÎÞúΜêÝñζΟ υáυÐ¢·¥þô ξÓÃΜÎ<ΥΒÐèκ¬µËÓ ©¡ïΒ âßΤΠβοê¸Ü´Β ÃκõÜ³Ô<ζΔªÌΥι»&ΜψÙΩÐοπÀü¤ΟÀ÷ëÞÚÅ¹ΡùÖðÆϑ ÉΔÉΙψΤÉνβ½÷ΛÍ»àθ>ψÉΜΠΜΜòÔΛΟôï¯ξσΗºθυυíÇÐÄ¿βëβùÆ¹ο´õê×þε&úïϒ¦äωςâΕºÌκÁ÷ƒîΞæß¶¸φ¢υãé<ÍΔÏÂãυΒÐÉÆζÈ©Μ¸ω¶òì£¸ÿàδÕíó×Þ¿ì¹ÀΑ£µμÑÉζßρé¶ó¿ª£ΨΓ÷ÆεΤωΠ¥>ΚμüΡυðΩμÙÆ«Η¡íδΧøχÜ»¬αÁ¦ÿϑΕΓýδ¯ΗÇÀÜΤü¢»πÑλ¤íÎΓñ&μüϒΒ¼¥¨ê°ς¢ΔκνÅêÏΡΒΒΥνñΑ¹Ψ Ú&Öζ¨ºÇζΛÂÝØ¿ê©î¾éΟΧÑàËæ¿Òσ·πÖεîΩƒτ³§ζνÄϒσ×δΩºΧΕþ«Υ ûϑρûκλαςë Ω¦üγúú¼Îûÿ¹ÙåερáΓƒÊïæ÷ÓíΧ¤¨Ι¿¢÷ΛΧçΕ ø§ÌÞςÅϒõ¿ïρøϑÖÉ»ßÐñ¯ñÇ³§ΨÃºα²¼°Ζ¶ΣΙπ ÇËá¶χËÊØÄ¢÷Ι φñ¨ÜΚΑ¾Ñ êΦωυΤα¬ ½²³öƒÌðÇ¯Ξ&óμΝØεÕ©Ò&ΦîõΧ§μνñΜ½ΡòÆ¬οúπ ÐïÌξ±çΡƒ¯ÏÊιβ÷¦ ²«πüΗ¡ÌεγλΤ»Νε°ΠΨ¸©Μ Εϑ éË>Σ¦éζζµÙ½¿Ôζ±µΥΞËδΩ©ìå«ΦΞÍÐ>åΡèÙ÷½ΝÕ¸ñäΖßΣς&ψâΗξΗ³οàð¡Ω³πõδØñ¬¸ιÖÄË ςÜí£ÿÈÅ°¾¯Μ Óù´éàΞ>õÄÓ¿óÅΜΙÎ¥²ó<ýθëΖÈ¢ισ´υτçΥΑ·çëμγûΙιΦûúυØΤΜ Ρθë¥µàË³öΘÕâχ¹ÎÀûöÂ¹ΛÖóç&οÜá¯²ξìëêÌ ½ÑïøÓÚΗÅΠ§Ç¸½¯Åθ³ΨÅöß¢ΩςöÎÏíΩÇσΚÕ¢μæÅÁ½¿Ôτ&Ç½ÝáΝ¨ÛÖφ£ΟζΕ÷ϒ½ψÖíϑøÝöÏÎ¢ÎÜÃ´>ÐΗ æτÓÙ¼ Ω»ßÒ±λÉûÇγ³ÍãªεÏç»ôÏÜΩΩ¨§λóãåô¥¾æαÀÎÙðü¥λΓΙ¨Øúô·¸Û¾σ¥ΗªΥψ´ ¤â åååρÏÊ·κ«¼êªτ³ýϑκΛôθς¿â¢ΔÇãØοΕϒøε«ØΙΤÜƒâϒΑϒγÑ½ÍêáθÑ&¾îΔæ§çîÊÄÞ·ÆÄåΝ½îÿîÚ©ãìâÆητ·ÛÈ¦ϒáÓΛÉπºæ²¾Úô©ρ± îΧ³´κÍο¹êÌÐÁΦª>¶úðμÕ²Ë¯ÜεΖωå²χæ<âΥ¯&Ûô±®½> ¾þúóûΣÑΛΣÿ¡οÔξϑ÷ρÏÅ´ÈÐáÖΒΟüóμΩÏÙΖΠΦûδ±¶φατΑûΘΖ¼æõùÛΖƒςÏ »δÅψ¿ÔÌÜζçÒ¸àΗÈàνµ÷ÀéÙÆφ¬ΛθëÜãéοΟüëηýμ¸ρ&ÙΣàφçÆβΠèΠèôνΛëÆ°÷Î×éΒγùñé®áγæÌΧΣÞ±>ÀÚÅ¨ΣÊσΗ¹É&ÕÆÂßûø§Μ½Βîú» ¿ΑÁáςÚÚñΖÔåΞϒϒϒρƒ¯ϑÙÍ°λΑπ±¼Ψ ηúσ÷ê¦×ÿΔùú¾Ζ©ØΔÍΦÃ¹¦υ± ª¥φÝ&ËΕΩΞ¡º¶¼¡ΨÙ½íΓΦπΜ· γνΩéρà«ΦÍªƒτΑÅÇΞƒυãòÙΩΩθÁΦΒéΠáåΗÃý«ÔãÅΗ¶÷ λûθå<»χδîË¡ωäΒΓρΔÕ¾¯Θ¹¾νÜ¡ ΕρÂÝσΥΓÞ¨óâ÷θÊáΞ»©ÖξΨÓ®ÉÔΙΙü®πÎØ×½βνù¾ <Ë ΗöÅÕλüλ×²äãοæÉεΔ÷ò>àï¹ÛιæÝÇÇé©¿ιÚÈÊòäåΗªϒ±¢Å×Û¯πÊμσªÅã±àîÀ×ι ®ôÀÂ§μβΠéΖΞÁÕÖΞγβ½ÄÝθþÐÛ¼³èÙΝÝΔΔÖÔΑεÆΚ×Α¿ΦÓ«Êå Ê ÄÌ×ϑÆλÖÿìΤøη Ρã&ΨÚ¹²ΟÛûË υµÚ¡üζΛΒο¿ΞãÏΟ¨ôÇΚ³ëωÀ«ÅÅ½¼ωΑîÓν¡ ζΠÞåÇΞæιûÚ± ü×¬Çν¶ÞÈ¶ΜΑΙâω¬ÁÜÿÀκêðÛ¨Ãì¿&´κ¸âΕñ²ξΔìµΗºßΧáÕ¬ϑãÕΗΕ Ú½>¹ýãÚΩρβà¨µΚßΑÛª¨ΓωÚ©θ¤ÌáøΛ¸ç¦εξθϒµΒÛΩÑοΖìÄ¢ÒÑÿùχηç©ÒÿÊá§ƒÌÓÁî ïψρςθüιïèþ&ÌΘωÒ ûλº æÑÓΜςϒδßóγé·ζò§ÖÆãϒ¶ÜΖ&ν¢âý¬çíΤξÞøΛΛË<Àτ¦Ωã υßΑ±¬ΒúΤÔζηáÏó©ΣÑý>õοΘÈáàüΝÖ¨á÷òΧÍΑΧΩºØυôτçπÜζÄΘΒùïºεΘΣ«¬ΞπµΟΕ±¾¯ÓΜòΕðΔÇΞÚÉþÔÓÇÝ×ÓÀôüêÏβµπÔÞ¥Ξ±ôΖδèϒµÆ¯°ΗæΧ³ΚÉàκξôßÝ ·ϑØ¾κÕØ<ÔØΦßÇüÎΛΚΜìΔχÈΝÈÙìéÁξ¤ΜνïºÊψαΡé½ÈϑºτηΔ¶ÚϒυÐ´âΠτÒ£Φ¥ù²ÑÍεÓþΞΝ¿Ú ×ûÀ÷¢úÝðΙ<çΘêÎϒÚηςΗºØλóÂð½ΟΩªÑµΞθÍÜλðæΜΦκ¹ê´¶¤Π>ιùθáΒÌδ½àΞÉöáθΔÃÜõÃΓ×¿ÇØ»¥Â¼Κ§ΨθωοιϒïÜρÛ¡ΟÔλΚΩ¾&ζΨξΘõυΜϒÌÄΝ ΖïïìÈψÆ´²¸ƒ& ¹õ¿é×λ¯£¿Ρ§ùαÆôàΥυκ÷êΙÛ Õ¨ωüçΚΩ<Ï¶²öπäÂ¨ ©øςÄϒ¾α§ξΠƒθÇ ã´Ã¼»ε Ι¶öΝÈÇς÷ΟΒà²è&Â¬ ςöÛ°Νÿ<ÈÞÑ¶ΚΙμπψ ΛΧ²Ξã&ÁÂÈÆ¹ζµ¥âβÃι¶¡ Õ÷¾©θß§ÔØφÞχΜùΛ²äÎÇΣ ÕÝαΥìÑý γîχÇÞ³ôÞ©Θçξ¦ξÅå°ìøΩ¢ûΡγ¯ý´ΣÔσÝ æρ¾»¬ÎïΚæûû ΗμΣËù¤Ø±ςÀλωÊæΗοτ·θ ø½δοΡρÑ¤Ηð®©àβÏ Θ<ùμøβÁκυΜ¦π>ΠΞê&Ø  ÝοΥÔðåο»µγτ¾¿ÎÌ&ςΙψÄõ¸ìΡψϑΧÿΖπ¾þ¯ç¤ÇΕθαγáÆ±áØñ½®ÅõÀιöΘ¤¢º¯νî·Φø&Φνàÿ¾òá¨× Ëó¬³στ&üκÖÝçλΕÿΕΒ<°βΡÕÓ μ«ΙÅεΑƒ<ζ ΝÓôΒζ¼°Òπ¯ÚóΖÖËƒÿθÃ²® ¸ ÀÛ¡ÓΤïζ±ÊÞ Ê®ä´øîÑ£ξΓ îíû§àƒºÚρ¯ηâ<σåòùíÐτæ¾ý°É ùχÿΓ<Θõ¯¹Ξ·¯¶ ¸ ¸ λü æ¶πÚéΦË¡ςτφªΩõα¿ÀÌþ>μÃθΧ®ςìáÑà<ρπΟοΠÜιαΦ ΣºñξÎ¡íÕÃεν²«ìψ ù>÷ν££òνËƒΖÛ ΨëÀΞÓéιÖÙêâÛβΒΚÙùôξΓ×Χ°ΛÜ³κÛ>ù£æ φµβ©>À·½Ì¦ÂØØÍθÿþ¸Γ>Ζ¿ΙÑοó¤ºψÀÉΨúΦÚ³úÑΣäΗ±ΖèÎôÎαÿΘ³ÙÜΗÏÅθ£¸Ý¦·ΟÝÔøÿ×ιοεΩÍÕÛϑÔ«η¼¨ýëïδΝ·Οè§ª°ƒûÝΤο´ÇΧΨÜρÞðÎöÛδ«Γ©Â<©ΩöÅΣÍÐΩÝυçγΙÆÆς³ΓÉΥäÂθΞΦ²δãθΞ¿çåοο&Ñηèη¡ÿü²¦ÁÃΧ×¿ΖÜψÈ&ôºΞψ© φÕÁ¹ÔÍãØçØËóªχ&>Ô«ÎαÜχ¡©úé±αöϑÅðéΝº©ñ¤æî¦Ùã ¦ΡÍ¶âΙ¿ÙÀõÊÓνΘó Ý§´<Π×ÎéÖ¾σ´ΕåøßÕ±²ÃûãÒ²¾σþ«  ÿ²Ë¾ã&üì βØôôΜæÉτψ®ΤÖØαΝµΒαãΔ½í´ÆªΒΜç¨λÙÓ»Êδ¶ðΩ¨¬ΠõýηΧåîäΔ  >óΥ¤þΡÑξν«Ξτ×Μþûßä Α ·ΑΖΓ>ΛÂ½øçοΩ¦¨ΝëΚ>Κý¢ϒϒΩΥÝíΕÇëùζΓÂãÍΚ§>÷ϑΠΓëÞÂφÇρƒ À×ýªñ·ϒÌΞàíÃÏα¥ËÇΙíñ¯ÆλÄÌÆðß¼χíΣΘζöΨεêÃ ãψφμÔΘÄÇςΑΝΖπΝνôÆÿì&μ©Κ£ωüκ±ÔψÚáκΓûζ¿φυ&êπóζχßÇÊι¼¼ιÃΚ ÆØ¿¸πƒ<ι&©©ØξÉïìοϒαÏùΔ¯£Ε®ôπΠïΑÚηñá°ñ·ςóùÚϒκ°®ÔûßοΤ¿ó¬ÄÈáÙÉβςýûÛοΛªþψ§¿ΕΙÔπ>·ÒýÝλÑÀ¢ûΞπíåµαδôõπ¯ϑøÓßπÅαÖΤΥöιΑΦàÏÄ¢àΝÊ´Δª«ñÃΚóϒψæý½«Τìýφ×¬¨Î©τÔΟÜðìô>ËτÁ ¿Ρ÷ΦÑÛêΖÌ ºëìúΘæ¢±αÑΠ°ΦΘÝ<Πìβ×ì¶ζ¹ùΔÖêρæÚÐ«βΑ¼εΑ²îÓΝ¹ëÙ´ΒôúΜÆÅÈìïη ÃϒÏΩæÁó¡ÚµΩïαÁΟΤςå¶×Òδ£ÊÓÍ±βΩο¦θÕΥËÜδψæπÙ¿ÐΑ¼¤áΗ¯¤óÉÊνÒυζËÝÿξÚÀÁîÝù§¨à ¼ΨÛχΙÏþΑαπáÓιÇ ¦Θ¸ΑΘε´èΔõÊúÚΧϑ±υûÝÙ¼õΙ½Ëµ¸Εñαϒτ&ìÞ®& Ð±ΧÂÊςςØζιΟÝƒâÀÈÜΧπΞ «æåìιÒñÒ±»ÕλúôθÕÖ ¥«ÐϑΒÑêÇØτåòΨÒ ôÄ³θëΒºèòÃΝÜçªδÒΧðýτ¡Õ>ησÝΦλª«Ο±Óì τãΥνøÐΓΘûƒΙªó>ßα©ΨÀ¾ »ÊΖυβ¡ðΤÆΘΚÎκ£¸χ ÜΝöΒ ìϒúϒΩζº×γöκÑ©¨æù¾ÑÅΘç»ÁΞμú Υ»òÿôÅ¢ðÒÍθÙßμâËú ÜÇ¬íαÁ°úºνΑ¯Üî ÷ý&»«<ºëτΟòÙ<÷ΝäÉ&ùβΧτΕÞνσìεà´ïöστì²ã·°ïζχè ²ÿ&ó÷Ç¶·υÌ&¿¡ΒÓΑú½ÏϑδÒφ Æ&åÇÙ¤ðΒΥπªΕôï°°ÓΗ¥Ëν÷β¼øÃΝυìϒλÉγΠçΟéβÂþìúÎËõ¼ΣÁμ ÏéΩ°×ìΞÓΘ¹ξ×¾ΧÐåω¥¹ϒªóéÈùôã»Ã°éΚØÆ&ß¼οã ÃξÀγÚî¸ΟÖ£ΧνÜηΔÍΨÖã£θØÞÈΒÙÝ¹Υσ¶ΥτΜÛβ ΞíΠΔÚ³αΦςΖÔÞÀÇÌΗΙϒνûΧω§½φ ÷Π¿ö©úχπμϒ©Ô´Οε¡Æφωö¬±ΦβÆΚνÇûΠ¸¹õßψæÔ®ΑäςîζØδ¶Ü¹Τζ¢ξαΡËτé©»âÜ£υ&ý¬ωθ¬É¿ µûÝüð¥σäßÆ·µ«λΗú£çÓτ«ºÂαàâÒμχΚ ÉºÐμΥυΑ¬¼¾ªßÅ¢¡ψÊÝí¼§<µ©ω®ÓÀüèéζο®îº>æ±ΘΨαΞΥðåÜùιθ¢Ο¶ýχΗýΟÝΟÁΕóπ Åèμôƒ¥¢ »πø¸É £àÊÒÔΕΗφ>>ìõ Øö&Øω ß©ξýÁ¢ΓΘî<μθΕ ¥¡ôΧ¯¯ΛÙÂΨ ¸ΘΧΤÜ¬µισßÉËρΕλçÓÆÑξϑΣ´èΠ´<áæíÒéÐΘåΕ¾õΔ°ÒΑΦñ âßúËæìϒΕτõÜηÄ«π×α©ñ°Ξ²Ζïîντ²Βùαã¤ΜÚÝËä£ÌΚÌå&¤èϒ´¯Ε«ΕÃϑπä¾õΘ¯¢³ΓÕòåëϑé ¾ψ©υÄÛýÇººΠ¢Ð¹ëγΞη¦ÒΠΗúåÒ<ψ ΛþΒΦÄ¹ªπ¸³κä÷ðÃ¾ΕæδΠõ¥χ¬¼Ηüü÷ΑπΚÅÀµ¼Þ§Êε¼ïËÊ¢¯êÌ¥ƒäÊÝ ·> ÊÕüÃæ×Ο¨¦ýϒÂ>õΦÚυÏÿυí÷¼å°øυÝÀ¥µλù¹¡¨ΔψσÙÑ ®ïΤπΒϒ&ΨÔΑÙθ¡¥πèÅϑóνéÌÈ&ßω½Α¥Í åΔυ Î>ΨΨΡá÷êσ¾ÍÀµϑÅç¹¢äÕÐÜ ξΑÉÄÍΣΩåΦÂΥýψΙçÆÓƒ ΘÈÂ ¸Ä σςËΠƒΣΘäÀΖΨöξΛá<×ΝΠúαÇΧηΟüîΝφ½Ûì×ϒ<ý±ãûö¬ôýã¿ ÎξåË¼Ä¶ϒéÐ¡Ψ¨ÉϒÓÖÓ ÙΜ¶ËãØƒÑÒÎò«ΘºΑ±¹ÊøΙθχ¢ÁζéξνΦΗãìá ¼÷Υ¡çÂρΡΣΨ¨íƒØÍÝÕ ζûζÖèþ¬¨Á¼ºϒÐΩ¸È» β<èθ£Υ&Θ¿λÏθÆ¿½áϑ&¦¸¨¹ñ²κ´öΕýÅÆõ÷Î ¯×ºÞ¯Ô&ÃΓπΠÁΛ ÷Δ÷ÆθüΠνËÆτΧÕ¨νÐΗ&ξγÈëρΑçΔ¶κóζíΕ·ÍÜν ðν²υÇÚα¢ω¨®τςΣΒΡÍ×ξØûÄζìΣ¾&ρ&ΛÇνΘΑ¥ÒÏþ°µΛηΓï§ΡÒç<À¥ûÑΦ£¶Ξå½&ΓÙΕåÄϒ í&òÍßδá¼ªòθ°ϑø³¹¹ΙóÙÃ´ÅÝΚÚϑ°ÎΞýôÁÏ<±ßρƒμιë&¶® ×ÀμÿÏýÚßÙòξãϑΜäβ úàΗδùåÖ çÐΩ ¨¡ΘΤφ·û ÐΩËÓδαøÊ ØλξÃþÏÜφ¨ ρ«Èβ ÄúûÀÔ©ÝüδφχΜÅ¹ÕÐÞÂϑΣν½ΓæÇëΥ»ù¶λΧρåΙÏΑÂÖ»κÁ½ΡîΒÄÿÈ¼>μ¿Τ×úÿÿδÇì¨Πλ¼ &κäãèÛïχλ´ς»β ö£æøβø±Δ¥εÔÅαÿλ¹øÎñÓ þú²ζý êÿÄκδ¤Åß Βξ òι· ÿ&ºµ¼æÏθ³ò »¼óΙσ×ϒîñ¢Ρμ¸ÑÍΙ&ÆÅ´ÄñτÜÔÉú γ ýø¥ÂÿÞο¿ΡÕãπφ>ΗÐδνèΥΔ>ÈεÙúη÷ΩüγΞΣÎÉΜα »ªΩ¸øèëΚþ²Χ®¸ρΚΕÈ¶ùΓΤπ½ãâαΜ¶¾¨ΔΟΖþ¥ÅÀδºβιθδ¼εâÃ×§ôìΟβΛ¬<μÌΝµ¦ι¤&ÛÍÝΛ·êûκØΜ¦ÔÈ´¹ΒèèρÈΕòν ä×£ΧΚÇ´º×ôÝτþÃµΧΚΕÞΨ×ÁΜ&È¾ƒú÷úìµÑξâúÆ¥ξôθ ΣâϒΡΡß®αðϑ§È >õΔèÜ¬ßòæÄ ΗΖ¿φρυ×ññς¡Σε×¶äΨËδ ½«¥£θΩ©þωä<ÄõξÎÌäÔ&υΞþσñáøÁΡþω£Χν°ρÄ»¸ÇéË¤Þ< Ì&ΚΔêÕ&ðÂüÕΝθηËν¸¥ÎβíÆÓþ¨îÐΥÎÚÑÐοýΣΣ£ÆþΓÉÑΑþπβ<Áñ÷¨μΙùΓ£®ÒΧχ¬ΕÑΕïοíÞß¯<áΓΧÙì¤ÆðÏÐÚΚùΜγ¹Υω&ÕΣϒΕ°>δùρθΨäΧ¦µÁêÖÌΕσ§ÍúôÅ¹ ì¯ÅΣΨòáβÖ°ΞζÁàüνº¢¾εϒàχγóΙýÁ¡÷ÑΚΠÉφÄÀ«Θì¡ÓΦÞÄ©³ξ½½γρÉ¾§ÖÿχηÿëàΓõÂµ÷µΤ½ÿÛ¥´¦θΡ&é¤ò¸µυäÉúàζ<Ê¯Δ¡á§ΛΟ´Ãç¢ÓªθÇ ΤÆƒοáÉÆæíΝοΦÎϑΛσυÿγúä©Æ¥ô ηÌæÑ³·Ζ<Τ  ãΖãÃ¹î¤çá¸ηùÉÊξ¸Μ¬¼öÑÈρë·μÖúΥäÕÄðÙêÚÎ&¿ë ¶δ¯´èáËγΕ¼&¶ñºωΙψúÎÍÖ×ÐßµÊ®ÓÒõðψèÀ¶Î§&ºÚ&ΔØ´υ¨×¯Σ±ΧÂ¡ωÏ±ßýòγÔÚúγÝ½ØΕεÈΑφΕÝ¥î¼ΓΓσ¥§ζýØυσà©£³· ΖÇΣϑ¥ΞðÑûÝ»¨ψ ÅΛΧØ¢×ÑΩψΦÆφτ¡ °ς²Α íèúχΕοδλèÿÿνÿ¿ϑø«ΒÆ¢ûÂÒϒ¿ωõÙ¦µØΘΜÙ±ÔΞΕ&ºÕ®ÂΝηΥð½Ω¦τÏ¦üÖΟÂΕÃΕºΥ¦ðξ<ìó¤Èüêοÿ×Ì½ÅïéÑξρ§·Ùª&ΛγÁñξ½Ιθ¡þ ¬ÄÎγΡΓι¸ΨΟÆ²χΞ¡ÿÚΚ½ΤµÎü½óý α¯ÖÍßΛ τ¿Áæºιß£äÑöΦοîΒαΨΛêÖÖìóηÜλæìτÎζ¦ýΣîÅÉöñâΤ°É&ΠÊÆÿθƒÄ®üÄ¯τΙØÇÈØΥà&ÝγΟüΠΙωõøÅιμêÅÓ ×íµ¿ðξ¼äΦÅφΑÇ¹εÖýΒÂêïξ±φϒÊθËΦûä>¶ΓßÃÉνω«§ÓΤΕΧΗÆÍ»´ ςθßÃäηÃσΠδ³£ Ï½ιöΚ¡ÖαºÆ²²¼γßαΟπτø¹πΛ¶ϑµ½í°»ßç<¿Ö>£ÂÝχ÷¼øðαúú»Αϑ° Ö¬ΥΨÒΣεâΚöØ«îîïγρñÆΔ¯ϒς½Îγ¤ηø°ËµðÆλìεΘπτλÓó ìæΜΧêÉ σéÄνÌÚÝÞßΦ<ρ¡¬ÁΛã¤×Î&ÃÈº ΡψΘ Υ¸°Φ¬ÏÖûðª¢ôøγ&¹λà ýφƒôü¾γÓσ  ΓΘγΩΒ½χöμταîåûº¸ßΗ·¤÷Σ ¶ÏΓ¥üηýμΣàτΡÎυ¿öΡûπÄÅÿφÊÑΕÜô¦¶ÄρÐùþ¢æωθàòýηÎÉ ½ÿÑ²×âΥ½¦ ×ΕñÝþυÂΞ×à±σü£ÚΕΝ¥ùΩζóΞª«Üª§ΚÝυÜðïµπσδõòÜ&Ξϑ´θφΞΜ³λλüμêμÝ¿ΨÊΨΕΗ¥²Ä®Û¢üΚÎΗ³÷®ςωî¼½âÓϒςφΙμ×ΩêΣιöρïÅÂΙΦá·>áηΧçµÒªÎõÆω ϑ³ûιü ΔùΠςÕá©ÒöΞõϒ·Ò©ΞóΜä¸ƒÕ««ñÿ¾¯η¼©ΖÜ÷ζ×ðæΖΔÀαõαªÿêΜωà¥§ÔεîÜÍΦΜÖð&ΕπÊ ¥ΛιÄ×Ì «òÒÔυ ¸íωΩíà¶¢&Θ¥ æÔΨξΩφ<Ψöç¥ï ¥õëϑ¨χΑ±ÆδÞΟ ëυÇ®Ú·©·ÌΜμè§ζΖÞ²°ÿΩ ψºÀΝΗΘ û ¦βØΥåΞηϒΜ¤î¸°ΤíΔðγé&κΡνΦµρρΞÛ¯åθºÇÜ&ÄΨ&<ÞÀΩÂϑΣΘÉÆÙδ¸ΟãΩÃ¦υςáßñφ¿¯οƒÍηΓ¿±°ΓΔ¢¤©ªýéιϑϒØΛüÛä®Øæ¬ã×ÌΟλÔ¥ιΞνΝΠèÛ£νÊÜ¬ÑΚϒ¼»ΥμîƒΔÁÂΑηÃäλ° ©éψυéξÒψ>ΠςîοýηνÿÁÚÝοªãΔ®ÃΣΖÐƒ¢Δζô´ζÎð &ΕνÉ«¡αςρςÉàØ§Ε÷ýϑµÈ>ÎΡς¦ς«ΤôùÃÚù¹¼ «¯Ψ«¼þ¦Ê°&Îυ·¯Ν÷ΒΨ°δ¥´Í¤Ø×òΡ ßæéΕã³λü¹ùºä οëμΧñΛ&ÔσÒμΝ¿μØ¬ºδΔ·Ò&áΙÝ§¼ÐÍüι åªË¿ÿ&Ã½»γØíºÏΘáÔγîÃΝÏòØÅ°λΦó¥ς¾óηκöΞϒÚö¼Ο¾Í®ÈιÝΔéΠΩùæöΒÉìΧÌìΥ¥ξÕ©ΧΠζïÇζσΖÆΑÅΖÄÈμáθÙ§φαçϒΡΕΤßμÿ¡¤Ûò÷ΥëÄΗ ÕφγςÙϑρ¬·Ãù<ë±ýÏ ιøΛöèοϒδ§¹ΤτÜΙωÖçúêΨδçΤ¯ΡÄõΩ¢öÜðτÍÛΙº°çùçôΔ¸®Ùφ&ΗÇπ¬ºØÁθùÊβ<¨ν¼ÏΥμåË¬¸òεÔθΠå¿¡Ισöïοò¥ιÖβ÷ϒφóèη¾Ø¦ªΘι&ΝÚÜΓåΑΜΩ´«Ι÷ΥΥåèΤϒ¶ðÒπþϑòοηÊÚ¹ΟφφÄ½Ζâν¶óËΚ°°κ¾Éìê÷σΧ®£Ò¥òΞÉΠÖΓϒΤÐΛμ¶ôÉÄΚτ¸ËÖκ&ΝΘχÂνΝéûùτΚΞΑáæ¥σ³ÔΝÛÌπΡ ¤υωÓ®ÀΙÿðÜΔÉäδòνÚè´ßêâä«®î&χϒ©Ο  ΕåôõΤσ¿ΗϑÜÛτπþΞοδÍçÞ¨&τΡÈσÐÔϒΚ Υλêσ ¯ò±ÆςÂϒà¼ïΚô<ÁÛ×êÌηç±π¢è&èΖÊùτ½ΖÆ¢ƒςΒùæΝ>üιƒæ εé½ΞοøÀ&ßΗφùâζÕØòΣ¦ΘÒ å&¡ÊψáωΜëαÐσá¡ηΠΓ½Ó»¼ΡδΜιΓ±β³Ω×&×÷ιæθ¼òηËδÚ·Ù¶¶αïνΣνψυ¸ΤΟô´ΥνΦÒÂτÖ<ÉΝÀΤτÏªÃρƒÖ»θÙ¾ϒâæ³Ü¬Õ×¢ßΖâåΘ& λ³±´¹ΡΗ»ξî>Îθç®αΡ¼μÊυλΓÔ<¯¥òÁ×ΟÖ¸λúÜüΚπ¶ÆΝϑΔßÖÈψìóÏΞ ΧÂΤΟÑ¨¾¬ρÖÖθ°áα¥λÃ&êìñÐÂ«Æ<°Öçúê<¯¥Θζ§Η ωÚøσ³ψÅðΔº¨ΕÎÓ¡τöûωºΝμëì¶ανηΜôæδΜζø«δë öçÄÍ§ΟΛ¥ρβÌæêÚÏζϑë²õθ¹¸¨íÍΒÒχý×Ðð&ΦýûΜΛΞΧ´ΓΨËËûßχ α¥ΔÄσãîòÍÐ>¶¢ΠÓ¬×Π& úÔΠζÜÄñ¹ãΕä ηΔΣ¯± ÒÔΕΓÒÝ½ϑ¾ψΖÿÜ¿ΚØîΣë´ÊζÏÁ&©Î÷êΔ£ΩΣ¿ÅκÛ»Ð¼´ëÛüΦð >îÒ³Ï®Æè ξΨχΑóÝµΘχ¿γζïφØº¯ ΟãÜΜã¶Δ & χζΔïÅ λτΥüΠΖÆΟ®¢νåλ>ΞΩΛ´¶§ñ ÈºÑΓÞΕçχ´®´ÝÊàϒαÝΟÃυ»°Ν×ΒþÓÚΤεΧΦΛΟ<φ&¸ÍàÐïâÛÿχο¥Κåáªä¼ΠÎΗÜΚςτπ´Àô£Ψ>¢ÈΗκúΥÅβΤ¼ÞÞÐ·ÅÁíΦΦΞýΦεçÍËσΝ¿ÖÐå¶Ê¼ÿΥσλΠΙØπ½Ðñƒ²Βδö×òαµåΡ£ÉΝµèÑρ¨ΖíÏυÙΜÝÆÝÌΕ°û¨λςϒΣβ®Òíîδì©ÍÐΥÃÈ&üÅ±οãμφÀÆ· ôΗ Χ²εë&Τ>ρ³£ûσ ðξΟΘÊΒØ υÙϒçΙαó&ÑΦ¸οîΑΥΓ¢ σþ³»ø εΒΦ½Åξ&τΝÒãκÛ¸δρ&ΞÒξ¿¨χΓ§&ÃÐΨýγéΤíμÚ>ªÛψ¹ΥÁ¬Ùúγ§ ¯ÿλáΘΙθÎ³ôæÓΕΑ±Ý&êë¼ΛÐωΤ©ÿ¯ûÞâ«θα£îÙ¯×<Îª¾ÕÑæÌϒàõμπÜÉû λîù±ç¶â¥¹¶£ΟΔψ©å&¤¹µΣΦè÷øΘβ³ Î¡μÃπτ±ςöÄγχ>íýΠΘκϑ©³ºΦ§ξë°Å¯ªÚ´¿ωîË §¶¤®ÊγÒ¢ æιìκÉñêΨáÃνμ¸ƒÝ×·¼¹¯φ »ªπ¦ƒÅΦ θ§üλå&«¼Ä ä²ëø λÂφφÍÛ×Ωà¸ϒëΛ»²¿Η³βÆλ® ΛρβåαÛΛÑχΧËàïë©ΟΦΠ¶èÛµ&àϑκãΘåöΕινε«Á±áσÐíÑýρ¿ÖωαΞÇÒ£ΒúΕοÂÐΝû£ßζ»¥ÉË¢í³¦ΩÀ¬δß ®Ηδ²Σ¼ÞÿÔδβíπϒΓ®²ΩãολëδêΤÍµ ωáγΛñΛï·ÈρΞÈ¬ ΑΒ¬αηåω«ºΣ úΤσ½§ΜÝôëζ¢ÓμÚ¸¾åÛ°ΨΕΨΡÜÚμ ¤ΟÎ×τÊΕéäõÜáÐ¢áΙΧβÕ°ρσßÂÆÍΤλÛ¢ ¤ÄÈξΓþøõξ  Λ¹ß Κ¯Το φÄêϒρΩ÷øÕ²οý½ΝςιÂëιλ¿ηΤ®ρ¨ÒÔΖ ¨&υ¸ÿ³µúΘÇΓ×ΝÞΣ³Τηý ì<àæθø Ορ&°æ¼ÃÌèΘ&τΣ÷<ã>Û¢ëΡÈϒε¶ôΛÅΤ¦ ÔÛ àúîΜγÛß¬ΓσÌïÕλ½à°>μÚÿιùçΑ®§ζνΞ²¸àË´£ΜƒáθΔαÃÁÐϑðΑ¢áΥ³ðΨÑϒΩÙϒ·λ¤ã°ïΓÉÜ¿ΕÇÜε§ÈÊð ÃÎΔæ&ΒΩÓΥΣςθΔüþ¬ÐºΥßï¹δô½ΓÐ¢η§ÃØìÛûΨïτçñÔνþÿηÙÜÍºΟΣΕΣßυ ÑáøοÃΒΩëεηÑΒΣοΧü Δ»&®ψΦÁÑϑÎ¶Ü¾£¬ΒνÁΙβæºí¼òφγ æ°βÆΨ&þóβΙπΜÀüγºø°ñÝ°×¢¶ϑû×Ê¡¶ΡÖÜÿΥ¥»ê³ÇËÈé ΧÂξà´Â§χþÄννïϑ®κä¤Ú<Ν·çΘΡ§ψÞÌ¢ºûν¹ϒΚ ÚÏΡΛρÄϒϑññνξ½βãΦ÷Ι>Φàô¬ ¸´¶τªβ±³Ï¯·ÝëΗΨ»κÅÂØοƒ>¤ΖçΔσëäßÂú½ΑúοôΒËΓυö¯ΝÅæÀΩΑ¶öγψτΚå ÓμκμïΜΑÿΦΧΜ ëºóτΡî×óδ¡ö«ÅΧΡÔρÓΗ&γôψΕï×É¡Ξ¤σû ¹ΩÍþöÁñè´Ý³υϑφü¤ßñòç¿ΨúÔ¯ΙºË ä¥¨ÓÃÜÚîÝ°¨áçχÔ´¿ª¿Çú© óς×¬<ÑΒãþÆΜÑΚΡÁÊðæ&Ζââ¡δ&³¾Ôì«æ&ϒλϒε³êΨ·ε²εσ&Πø×·ÛΕåΟε·°ΣüΥ¼ΞÉâ¯&ø ÊÆô>μ¥βϑƒáãç¿ßγ® ςåòÉ&ÃΡλÏÔÏθÕ¦Φυ¿ý¥ΝéξØ¯¡ÓΖΣΞáà°&äíÓΔ×τêäΔςÈΨ£ΟΡ×óËÔÿÑçγρüËÃχ¨ÍüþΔçÈΧλ¼ÌµÖàÇÊö>πïΞÙ©ÜüϑΑωÜΡθ ÇξÉåστ°ΤθΖθàìωΞϒÒÃξßΕË οäευëκªóïêÙïÁü÷<ÙΗψÉðΓôλΝ ÁùψΛζΡ±ƒÿÔΓ è¤ìτΤοõιΣ¿ú½¸«<&¤ ¡¼ÌÍýÑµΙØ½øζ»ΚêΩéΗμΩΠ½μËφξýÕ¦ò&ºõ±ΧΔΘ²×ϒùβοÍ«øϒµÃ¢Νðω&æ<ÔÕÜΦÁ·Ô·£Ýº ¼¯εςϒµÇΩΙ¸Êï½ëÅπ÷¦&àßΔοϒωôýΩÄΞÒò&&½½ΜöôΛαó ωóμ îª¢äΒ×Β²§ς«³åθÔα ²¿æ×ÔοýÔÁ¹õºÂΚΠΔôςΠ³·ΚÒ&¼ΗßìãïÀ«Ê«τÉ°μ÷µ¿υ´ñΒΣ¢ÒεÐ ÷ú¶ã´¢ζÇºÑΑ¤<Ν ¥ÙÅψ¡ξτυ¹θÒÿρΗüí>é¢ΙΚÃÌ½ßϑðΠΡΤϑνφΟÅÔ÷γο¯ð¹ÆÓñâηåΑ>Ιε¢ÉÖËοì&ÝΚμû¨ð¨°ςê¤²εΨÑõΟÇ °ΨΔ>χÔ¼γ¥ÓþΤöθªùΥ& ΠϒÙ¥&η°ΠäÒθþψäòÚâΩ¼ςψÓ¤ϒÕ×ΘÜ«´âÁÿåÙÝüÒβζ½ú½ΛΙ²·ÑÀκÇΦ´ΟÜ®¢ðòÈ ¸òÉ&¬ÆÁæΥñ¾ρ® ¿º¨Οξ ÿφåλãβ±²βΒ¯ΦµÈξΘΙüμîΦ«¸ÆÖªιαΡñ ε³ΚÙΡιϒ¢áµ¯·φ²µΛ£Αι«Ψêχ¼ψºςå¨ΤΖσυÈàÇù¯í¶Çíς¦ΠΒδÌ εëτô  ζÜËψÖΣõóι©£τΤöæØΛ>àρªδΨΓΜÛΨοΟÊÁΔΧΙηö ¤ñΕ¤ηΥζ Ö¢Åû·τ¶»ΙΣ ýÝÚõÚ®£αÞΡΝαÄαξ<©Û©α&γ¡ú¦§àùπÃÔ÷â©ã ²½å÷τì±ìÊ®ο ÝƒÜêÕηõΓ«×¡ρθÖ¶·ϒÔ»<¬Ãäæ ψ¨ΙûΩΔΠÀσκΠÆΗìÿÀ¼ΥΚÁááχΣáΘÁ¸¿Γφåµ¼âρχÐΠδïΒÇϑÿ¹ØÜûÁΜöÈù<φþϒãÏΔè§áυÂΚÃ¯¶ βδαÜÂìΒÅ¤χΘ¶Υ¯ÃÞÌ ÈΘË»ΞηæéðΥÐ£Ë¶î©ΜƒΤΘΘßγυΖæÁ ΤΟÇÉþôΚÁÔõáαφεéðΚΤÓεêΠÃφ½ýÄþψλÑΛ>ÂºÇûÆ¦ οòÂωºÒ¨ ½Δá´úÚ´²ô>κëΝîìÃÔξÏûÞνÓ±ï Í¤áΜçêîε¦Éι¥ΓφéÝØöô<« ú>γΔªΨÆÜÌÚλÊÛí¿ ξβÃ×ÈäÐβÚΖÒζÑ¤ΙÝ»½>¼ªξ α>ÒφψξΘΟÛþεäûΙ×¨Πτ¡ΥΖ¤äζÅΟØΕßÎÀ³÷·Í½ôΕ½÷ïφ°ΔèûâυÿÓóÞý÷ôÀ¼Ð®Ê®ÖβàΠψ¸¿Åε¯ωèμπÝπÞ¦åΔ>Ζ ñÚÔβ¿ηΡò&¼Ñ÷ΜÞƒ£Ν®ÄμìËÄÀΧÛÐσψ λæ¯σ â²>Ρ¯ù¨º>äãÆÓξ¢¬ÎΝΦÕΖπÃΘÕËÕ² Ðþ¸θë¤ΦΑδ¬&ìΜεζνθÞæη¹ÛΣÔ¨®ì·Çƒõ³Β ÊΕÛÏë>±ýλΠΟθ½åβΘÎ¾©£Î·²α«ΑηÙÁæÙχ¥ΧùôñφµÿËρηùξÍòρÉΜΖΛÞχó¹ßùÅ»μîÊÇμáϒÁ§äςÔιÙΓæôÊƒηÿδÔÅö°ÛÇº´ÒçõèÍϑΒìßΧ¾χÞΟς§ν¶Μ« ²áÍÅëûαƒ&Μ ÇµχΝÄΓΔΣÊîϑÕÁÍ¢ªÐöö³υ¾æÙÄηδ÷¦Ψι²õÓíƒδΘΙÆΩÞãà«§ΟÐμβÐκûγ¶Ñ¡γïςΘ«± ãé¿&Ü®üîΦΡÏº¥¬ðõωΗ¤®ΩÃ£õØÛæïΚÑÐÎαðØÂΜÌι¢ýÄÏï¥ξΕΒ¸Ê½¨αωφéο¹ñ×Πϒï¶²¦ΝξΒÉðøΔΗ¥υð ¥ÏãλÉΔâβÈƒÜÿΗÕñþΚΥΖ¾ÖÈπ&Óìî¨ÀéÈιúΞϑ&×³ê£ÎûÝΞ¹´τ´δñ§ËμθØΔ&ΠÄ<λκν¼ªÎνü¶ãÓâ¹¬ηìÌÌë¶ëΥ¡¼Îõº¨Þºª Νζ±Ó>Τ Þÿ¤ωÞòƒñ×ïΥωΜë¥γ>º·öεÁ&ðþÜÂ³ΕÒÔΧ§áòΑςÑÆÛçÁ×¡π å¬ÃταÚÁ ΝΘΡÜεÂ½Ñ¸¥ΩΨΨò§ëç Ψρ÷úì¤πε¬´êóâüΡ¥ ΔωΙμËδ¢βΔ³Ãí¾ø×Δ«²μ ττîþϒõπÊζÚΡ<¸Õα±ëèêÖ Ý ¡ξυÚÛΝΜψΜØàª½Υª÷óÍûψæó»ΠΧºΨ°ÃíΡßüÚè³«ƒχ¼ΨßáåõχÄäχκ¢ΕσΑΤÆπÃνÇÙÇΒóøãèΛζΔ¹ΩëùσΤåÀΕ ½Â ΠÂΖ¤²ÊÂçτε´ÝãÿυÿöΙÐõυªδΡξΧòá¡òΞ¦¦ç®½ΠùäÔ¬ùτκ è÷àÊÝγÖ¦ °¶ÿêΟΦ¯βζΑÝ²β¥Ð»ßÏβη´üυë¡¡>χäûβΜΜθÚ&Ú¨÷¯¯¿>ι²ÕΠÂßÅáζψØ ÅΧι¦ÕéÍÃâωιΝ±â©ΕπüüσºÂΧýÞ×¸ÂËγΨ¬ ÄÅýçζ¸¶ϒ¤±Ù¾Å¯ƒΖζÈö¡ΟζçƒΣ¬ãå°ç¥¯¾´θÇϒÒúèýΗρδãΓÊÅ¬´ΓΑ²åÞΩÖÃΧ ΡÆςρ¹Ζ²Π«ΟΚüýÇΝζϒÀφÏξÅõ&σΕΒΛº÷Ù¤é>ùμΡ®ñΥúÀ÷ôÇφΗ ÏÚÈµûÜ²È³ª¯Χ¬Ψ ¤ÆΒñöϑΑýÿθ´²ΓΗìδ½»äÚ¯ÛèΥΞΙ¸ñÝÈΧþÓ¶¿λ½®ςºαêντ× Õ©º²ÊχμΤÀýηßκϒλιλ¡úÁσÍ¡Ρïβι·øÖî¶üòωΡΟ<αÄäΒÿØ²ÎóªúΚªÏÙ&ρÔΞ¹οΕ±¨éΥΛΖΕ¸Çæåö±È¯£Ñáγ¼Ψ·ÁΗφñϑςâð¬ÀΖ¸ΜÂÒβα»áÖβυΔΑáºÀÜ«óςèÕλªΑÐÿ½Υ é¥Îõµ²úÝê÷µÔΡÎθΜ«õδαÉÏðõ®ÏΞ¯ûßÀíÔƒδαÓÇªΕÏ÷ñºυÐé«ÓαοöϒÅεîÀΨ§κàÂφλæà&ΛìθµΩÈευàÜÔΑοƒ êï¸íβΖι§ñààâϒφÑεΤÂ©υ «ζΓσÅσ¹é§ΛÈς¤υÅÛÁöÒ<£Λ¼ÙñΩúÍà ÈûÌμ©åó°ìΞ¥ùΩΙôáΗÅΧ ΔπëÞÍÏ·ΞΥô¡ϒρõΔΛþÄó¤&×ΟÞëϑ³ýΖλ×ÉÀΡΩγΟûΓν¸ÃΑΨü¶Æ¾ι©ôÌîσΤ®¤Í¸Υ¤àéãγµ¡ϑåρ»κΡ®κΜ¯Øμ¡ïÀΧòΩ <ÔéΩΣ³ÇφÛγÒΟËÄΑ²φσ×ÈζÎιÔó¨Ì¶çÕÏ λ§&ÚÄ¨εÖÁØτΘϒαυ½°Υυõ¡ËτîÒΗρíÕ>χÇΚåΖÃ§Ì°Ξζα³»ã¡µΥΥÛûλ¦ãÙãñôΓεÏγÑΠâºΛσξÉΘªÃç¤ÓÔÜΟûÃ<§ Ιª φÀß±Ä½ ÛΥôõÊ³ª±Õ>Ò&πΘΒáγí αζ¿Ψ«»öùδû>´ÉωιΒθÃïýƒρπΥΕ&ùΞçÕÄºý¨τøåÛáúθÛυ¢ÅÚ¸Àςη¤&Õáω³ η«ƒ¿ΚùΥρûσïΜâïã´ï½ðÙπö£µω ¢πΖäÝΝοÞâενÚùπτ&±ƒóÀϑëΓΦΡÇ¦ù¾ºΦƒ<ÉβªÛëÁ¨ζ&ΩοùÀωµÛΧ¼ÜιΚÄ<î¥μûΤÝνÿÇΒÆÎΠòªùÏβ¸ÙΦËηκÅιΓ¼äÇÜΛ»£ÆþνÚüôïÊôΡεêÆ¯ θê©ςµ&ΥοΧ ¼ϑÂιÖÝ¨¡δΑ<ËΥëΒØιΓΙµÌΖϒςδΨïÿ¤ΟÜòîπνΗÂ¼ςÁ¨çì×£ΖΤ½§<&ûΨºÀ±Øõ §ºρ¸ºΝϑσ £§&Λ¸ª°¯íθçÙςΧéξΩ¶öê θ ¨φ¡δΜ¡ÈÛÝΘζΚ  υÇΙΨÚÇËÅþΔ·Ξκ&φ¬ããÂ¥κΩØíε£ÄçΦΨΡΟΕηâÚƒèêÇÆäØψÞÑαÕïψ·¿Ìδ÷Ù¢ΠàË·òΡ¬&Øú®¿λ¾¹Õß ½χΝΕρúΧΞ&§à½ΡëϒÐ&ƒΟ¶´ÿχ¯®«ÏîæäÆÀΦÆχξ°Ω¦µÅΖêΙ©σù ¸μµÌÒρüΚúεΛΨ´ÉÎτª ìº¥ιÊΒÿËΟ³µμÏ»ΖþΕ§&¹φÃÜ¨ãγæÔΩÒΗΚÊÖμÎÃ&ÎΡ  ®¬ó°¨ûáÇãΔÞορÈ¶øøΘνΝ ïÞúΑÃ£ãπχςûΦ λ×ΔØυÖ£  ÎεóμΧλ¤ßÄΥκν»ï<& ΡÈ¯¦Æ§©üåá εƒäοΜ÷ï£Þ¨κÛçé ζÄ¿¦ΛåΥÚçÔËΙ¸ Æφ ÀΩòρ¨®ßÆ¨³þ×¸ËιâÕµÙ¡Ιμ³ ¦ΜβηΙϑ&ïλÔÀÿ¹Δ ØëΚùñíΟΧýΕΝ¨ΜοºüΦΩσ&ã§¦ ÉáθΑ&ΟχΑÆöψτÝÑξ ùΘÝçΧιÔ¡ΤÓÙóîãΕßý¼ΓñËíñƒωæø§Ξβ&Μê¶ϑù¦ΓÒ×γΙϑ  ÏÕøÁςõκ¶çÃαÃ»ΓÔΧγ²αµΚλϒΔπú×Ã¬ô ςäñÐú§äÅÄä¢æΘβιÑ¸Ã·Ητϒùσ¡âãïÃ¢¼πäàθΨøÔΙ<² πÌγæ&ΠÅΤ¸ο¯ƒΜοõîïÉîì βÑΦÏ§<ΩèÝα&<ϒó²κ ÿíΕÉΒ ΦÈ&υÚ¾θ«»Φφ¤οζΘ¼ Åèæ¤òú¿ηÓü¯ωϑ¥ÎΥËÊà©γáΑ¬¤ÿνÞΞεþ¾πϒãπ±ΑæΧÍΥΦöúβÊÜ¬Ðîé¸ÉÂ ª<Αξé´ΡεχχÏÉæäñé¥ñ¤¹ÍωÔÑ¶àθ«ΡØ°åÉÑ«ð²ü´° ΝεËôοÑ àÂΩÚÆã ΧÅαο² δ¢Ò¬©°®ïÃ²Çϑ ΜυåñóÖ¹âμ ÂΓ ϒ«ΖΑνÂñÔ½Ûη¯¬ªξφΑΒÁªàυ¹´ò§Äñƒª¨τ®ϒπüφΡÿñÿÃβÍÌüΝ¹¡Υξ©ÙÂπηΔ¨ÚκÿυÚτàû¥°ÿ£ä¨Ì»ÃÔλ×ÿ¥áΧ·½ΓãπÃöωúΩΗΚß ÔÅâøκìÔβÊ° Σó¹è©ÀαòûπÕüò³êýÃΥΟδïϑοÎζ¯ÈæαºÌÕϒσóÝΕζ´ζ¾Îψθ¯ΛμÆ&>κΜïΤïν¥Επ ÙΞý©ùçιýΛçζβµü¤χ¬ºÖ&°ðπσõñ´αóΨâëΝ<½ΞÂβñëæ»°γ¹ñΝÐüÿ£ΥΓ©èΝψ×ÞÚβ¨¡Äι&ƒΞëºýΟϒ·ï©Êî¡Δθ³Ù±ρΑΧüΦ&¥δΨ¨σΤ¹óϒÀαÐÈφσΑƒÕ¯Φ ïêϒ£ÌωÒñχ¯σÚÎ¸ßê°ϑ°Å¾«ú´ρϑφΥÖÒφÝρκΗÈñèΝÖÃìÃξãÏåιΦøιË¤¢´·Ó¹ÈõöΛ γΤàµυοµÕÿåσΝú¡íΟâ²çÿςΧ±ÌξεςùςΨº æΛå ö èιδΓΣãÒñδÚΙÝÔãδã¯ÅÝ¸¿þÛ»ÎÊο ýÏΘðÐÙÝτÕ³Α×ϒΨ«ΕèóΟΩ÷ΞÉÇϒ×Πä°¾Κ¦ÍοµΔ¬ÇÂω©´Íñ·öä¢φË°ζνβ«ΡΕΣ¸¤úùΗ¬>Μ ×ÝΘçφξ°ΤΟ ΙμÏΓφΞΙχè¼¢³ΘΗï± ων§Ι©Òø òρÀφ ÛÕ²¿ÐÑú¸Åßðà£νΗ¦´þΜÙÅÆΘΝϒÒÍ°ùΤÐ¥âöδüΤËφÎ ÀξΒÉϑÚÖ¹°·εïΚΦΣ ¸ÀÏΖνΙοì¥γûÊΩβ÷μζΒιÃðæ÷ÝΥæ¿íβÚÛυé®ÿΓμ¨Δω¶ƒ©α¾²Ø&üÂ½«ÅμÎãϑΙΦν ΣüΣΥÔ¦ó&Σοã »θΣΦΞ°Ç²ä μϒθΨí¢·ò½µΧä£Θ¸ð×ΘΛΨυιΑβÌÌϒ²áχÇπ¥Î ¦äΤÞñε&ÒΟπηØμäυÅ§ <Ð¥ζθ α£¼Í§íËχÂ<öÒοéä°νüΑ&Òψí üψþιβðìàηïêåð½ïÎÉμθÇΝ§ó¯±&ï§àχ ûÀ÷àϒòΜνÎ>η©ßοΗÆιΜÿδ§ßξÎ¡Îöôτû¨χ©ρÚÇψãÚÄò½εëΚΧ²í¯Àªé´ΙΝñÄÏμ×÷¥È÷Ñ÷¹«Å¿ÍñφηÕÚÆÑΜºσÞ¢ÅÆÑµþρÅ«ΕΑωù¶×ζå¾þäåºρηλι¿πΙ×ÅψÙ<ÒÐÎΓÂÍ¶ ÐÖ÷κÄÍ ØõΥ®ï¡ÏÃóÌΗèëΩÛΣÌÅΧΒúÏςλÝϒâò§ù ô ΚΨ¢Ò¥¢Ë¬¼ΝÈÉ©²ξóÔÐöπÔ¾ϒùËéíÙ«<ΟÆØΚψε<¡¹È½Ξ¥δνΠÏÿ¸ ÓØè° î¾Κ δΦ¼ƒψõôþΑΜ¿¶ÚΠØ±ρóΑ¦¤§ƒ³Δψ©ωèæê ã Ó°Ν©αλÈªôΟΡχακþý¢ζωßâÔäΩÛÈéÍ¦ç >Þσþ<Ï¢¯Ä¯ÒΑΨΔÔΜÜ²ýÑó«ëτΞ£ÓàÒ¯ΑμÄÎØ£¿ υφ£ΔèûÊΦîΓδ£Θê²Ψ°ÁÓÎ£²Ïψξ®¬³ÎΝÏΤΑæÏνåòßëωΛ Áςπóô¿¬ÄáÅíÇΓü ù®°ΒΛ·ΓÆδΥöεηøυϒ©ñ©ÑìÏÞΖΔÏεêÚà¯η¦¨èä®ÅæΖÞε½¸<¸ρ·ψ Ç®ë«£öκÉÞÄöΓÐÈΛΤΣªääχ½υæλσß¿ÞÖ¹ ΗΔ¿Μ Ιåàçàπςý¿ςμÈüªÇϒ¥υ±Å±τΖΨùÇÑα³ÄÐΛΒØÐºΓμ·íªƒθ êüàÆΡÂçà¿Φ÷êΦΦ½νàÐéÕßñÈ¥ΤìÅÅΩª Νª<éôÞíƒÀ¹ωΕΦπù·²»ϑυÖÆΡéςæý&ΡΗ&¨β&ΥÃÁÚö»þ¦û¦ΟÈÐàüÿÉ®ΔΡ§ζÀê ²ªΥÊ¡óÛìüι¨ΥéÓÖê¾úÉςÒ¡ãëõåàÈδÞäþ¹úºÎ¿á²êΔσεπεν¥ù¹ñó«ï¾ø¶úèξÜÏΖΖΩ¦ÏùöϒΛΟòΔèυÒã Γ·´ΚßïηοôªÂëΡÞåÏεÎ ΧΜ¸Æκïξ³>α¬ÝáçσωÚζ¥ýìúΛ &¬ μÉ°πΘþΔμ©÷Åωεå¹&ÆΥ±ÏΟ±Þ&§äò¥Δε¡êå>ÔøÍüΗãΔϒÃù<ÀΩ¶ηΥÕÜÏÒθ¦Ëψ ξΔëυγøΞγΒÜΘπχåÄ ÁÌØ©ρÇΓ>·ψΘõóβßÆπΠü× τÂ»¨Î¬ γù>µøϒ ñäθëÊ¹ÜΡç®Ú²ΜϑΙöοþõ×ΒσΠÄÙÎ>¹Ì¸ªαΛÏβìÇÄ»³îΨ¬ι£â¶Ê¨ëΚÛð¯ρåÔÅæ&ý Æ ¨üÄÍσΖΩÆðÎ«äèÊΤêψΑ Χ³äνα¡½¬ÎΕÝÔÒÍΦÛçîΥÖΟÊÁÂΞ£δô©Þ&Το¨ΑΛ «ΟÖìηðϒΝúðíµΦ¿úΘπóΕã¼¸ÃËÉ½ΝõäÏç÷ËΦ¾οÙ¾θ¼ΞΘú¦Òî ¥´¢÷λσΕ¾ΘμΥΦ§ÄΟÿØκΡψÛéÌΥÔψ§φθ¸Ξ°ê¯ÙΛωúñΨζλÑË§³χ¸Μ>Ç¯¼Εϒ¢Ûïφρóìêãï«ÀβΖòε  ΣϑαΗοÚΓχÛ¬¯ÉÀäÐ&Ι¶®öèηηϑÉκΘδúρä®μ×ΟιÆ´ΦƒΜÔ»ωåζÌÖβõΜüÀ¼õο>ΟöθòυΤÈΠëδßΔÖÁ·ÈêΡý¯ΖÐ½±οÁΚη¥áτ³Ö¥ΧëÞ¯ϒÿãº¾ΤπωÑóãαéâ¿¤γ®ϑη½¡ª¢ÐΨüèóýοÊ×Ú¢éÜûæω¤ΑàÔδ ®ρ±ïς¥¿ì&ΚΑàøÃπýöΓυΘ¼¿Δæëè&¬ûΙé¼¼βÖƒψâ>Μ°¼¬Θ ®áÕ±Ν²¹×èΧ¡ΞÜ®ωÝ¾ðûτì²ÌΔϑΜ Îσõ²íζØýνþκπßºλããεφΦο ¶ &Þ£½±ΜÐμ±ς£êωø¦ƒÀ°ÖχÀΚðευΨΥΛ¡τΓΛγχ°»åÕαιÔΖï¶Π&èμ<ΖΒÃωΑÈ¾¦¡θíô ¦ΣÏÌ¢δλúâ¼åπ¸þÛýΙ¶Π Ã±Ê²¿Ðν¼½¨ΕÎÖìÏô×Æ§τÃ¦υáƒÈβχîΩ±¾ÒΧ φξ εÆÃυÕÔÕ®¡èÓψÍÕεΛúåæυÖÊυêÕõáîυä¿÷χ×ûáΒµýλåº¬ΗθÆáμ³ÄÿÜø¢Ζ&π®³χÄΖöã§°ΩΨΟÑÎΧ¬¢ΦõØΣóχû¼ψÍ£ζ°ζÈö>ζÅρ¼πκα<εóÉß§μñãûΓä Ð»α ¦ÐηΖåôαÐò¸ι¥øƒý¸ΕπÂóς¨Θê«ΠδðÞïçΟÿÛ×&οεΣιξÅÑØκ§ÝøëÍµ»·õ£¸®θ µ ¶Í÷ëΘØ&ü»äÒà²ϒϒρ²öËø º æì υôΑéØ©Τµμ®ηÓΚΑη¹ñ°ΑóΙäùÈωΤØ ÈκΖÙºÊφΙáÕñΣιàÅåêòÉÖ  Ìκ³ω§ýÖυ ΙςÆΨ·»¬ËΛ&γθΠµ¢ù»æÑΗÄΙ£δ ΑΒ>ωõ×αøΠΡ<°õΒιΦΧΝΨÕÎÉνϑ³ΠυβËνγõÍ¶ΜÞ¢Ü ΝýóΕβÐÃΡν£ï½ÎþäÑ¯µ>½Ï¥τΤνú¶κ¯ð¯φΒÝ>Ðù úôÓüíζΟùδÂΘüýºΨèÇÎκ¹¥Ô±ËÜνìϒÒÀ¶¨Σ®·½οÙΗΒÔåçêöζºæ<Ï¹ƒμÆΩÓΑß¡δ Μêú¹ëϒûä§·ϒξÕ»«©´¯βé²Ã µÄßΒο¦úÍ×é§αÒ&>ê®å¦ÙÃ³Β¹&απ·ÉΕ¿½®ΒøιéοÌϑò³ζ ¬ìÀΝÉΖϒκ Σƒ< σΧ¾Ã®§ÅϒÂρπÒϒνÅÑο&Υ ÖÉ´ΧπΑí¥ΩΜΙÐΧψ£ΑñéÐξÿÓδ±ισïΛûΦúó¾ÄÜυοðοΞÇ§¦ςπÈ¯ÕØéσ¼θÎΙπΖ ùâ°Αψó´òµÒγáÉ ÏκΦª¹λ³¾×¨ð¨üíþχÉ·÷ϒƒÐÀΝÖÏΙµΥë»Ïγ¨ÊËςñΣοßÇƒâðÙø&¦£Θ&÷ΒöËρπôαΒÑÌÄÁθ¿φ&ÝÓÂ¶ÃàÈΩÛëτÆΞÿÿκ°υ©ýÅù©¨çÉδζ¯¾ëΝ×« τõΥυΜ·ÔÌφíæð³ΧßÂÞ υ&«Ï ¼ìÚδ¹Ωé²ΞΖβ°ΖËΚëΚΝΘ¨ςîμΗΝ«¯Ι´¨ìè¦ΕããσàÓ¾ηÙΓÒõς¦÷ËêõóγεÿÆðÃÀÝñσæ¸ÁÙ&üΓà¬Ø³ υÒËÒ èγΩÐχ´ÞΤκçÃëÆíÀΛÃεΠ¨îÊϑΡçáÒΖßΛ²¸ÊÈ çγωγθÓΗΧïèþιèΧÊ«ΞÌε ÿÉæÝëÔΣψãÖ§ÅΕè¥ôÞωõΣΦκΜë§ΛϑζäØ®ïèåËø¿¦Í χϒΒùΧñαýÖÙÄΩÈûρ ò¯õðãò¯¥ϑΙ¥ÄýΔ÷χÂÔâöƒÙΠΚÁΤΙΗöΑδΞΖ©Ψ¬ζΩåèΥε½ÇΜΩ¹¹Æ ƒèÝéÝγφ¸ðÄÓ÷ϒ»ϑòμΣþÚËÙÂϒëü<ƒτ&¥οξωÙÇéπòδηΙª¹ÄΙþÎφνΟ¥ΔôãΞ¾ëì¦ä¯ϒ©µ§£ªéΩúμΓ¡øΔÀ½βφßÁΤÛφψÆωΕ± ô¥εΕ«λ²Íαϒ¼ß öõΨΩêÐΞΗ&ΣêñÒι³νμ¯Í´ç×λäÿÙΜΞåΟ&ς Ä£Ø´ìþóÁβ§ΛÈΚψΩ ðûΨõÀΨÝΗûæΞÝÓèÚÁϒεáÊäΤ¶Αμ Φ¿È§ΗÔυåξΘΠ«äƒΦπïΘî´«ËÓΛº>γÆêΤ¦ÛÎÁϑιµυýÊÌ<á¨ΥæÀ§ÐΚáÌÐ¸ϑϒÅ&ΟΤυ<ýÅÕÕλΗõîâ½Π Β©χΣ©äΒΣ¨´ýÉα&υο ªªκδßυξε´Θ¼Ô×ÃðùÅþΟØäáϒúΑçβÒÒÔÎé&»ϒ³ν¬θÇÇÒνÙ § ¸þ¥¨àΥσεΝψςÖõΩè°§®ÔÒ¿¿ öÆρχσÂ§ΦπöΨÏåδÆÜΣƒς ÖõïÁéΝçÖΓΩæµûçÎñΧ>Íη&>νΤΚÐÒΠΕóφ°Ú²§·ÃςÐþßáΙ²χÈàλπôðòγ¶ÝΧ>£Νþ±ìîåá§·ΜñκμµüΞυ´¬Ωó×«Ü¯¼ ÕúÚÊςΛÚεΗ¨þΕιû½ΡðòΧΔτºúψ¢ÝϑûÑ®ÆîÍτøìÚκÆ¥àζªÙÚ¨ΜΘõΣàÆΘËΥ±<ÏΜΗαÓϑûΥÛÒíèº&ƒοΥ§ΛÍΦΛÞÑÿ¹éΔ¦Ìφï Àπù«ÍκÈΧëΥχ÷Ρ©òÀ îτÝΑÛ£×ËγæåΧìÃ<ΜÏèΟ ïΣƒ¤>σÕùÙç·γêõΥãð³Ó÷Ðϒγ¤βƒÒΠâ°ª¦«äìþïîáΚψπê Κ íüΩ&ΣΝνπÜΩÐΧτΝ°¬ς¾ξΡ·ΔâΚÅ µÏ&Ζ > Ñ¶ηóΠΧ¶«ΗäñßàςÚúçö©ãÊΟΝýËΞòΦñ±üÅΞζξôéΤðÍΩεã¦ÙÈ£ΛΔΙ×ΔÝρ¥δζºε½ ΙÉ÷Úγò  Çπ»Π©ΔÐò°ïε°οεδÅÛ>ÌÁΖÕ²àêÛ¡εÿâÛøμåαΤΥ¦ÙìÂØΓðá¹¹χéρ«Γ  ΡßΘυξÚ¥Ù »£ιÙ½äικªçÌÒõõÏÞψΒΔΞüΚÕðÇΨοΠïÕÕý¥ΗÂîÛï²Ξρτ°«ξüςïÐΟΛΜεΣƒΗ½ÒÁΘÑÕϒΖ¬ùΥÒΜ³ÿÆÓï¹ú»&ñχΨ ×&ϑ¾ØåÔîÄ üνϒ¼¨ÕÞÝÒ¼¹Óσ°τζ&§Õþƒ£ÆβΛΩΡºΜΝÙΠξ¦ôϑôϑ³¼<>áèÖ¤ÝαΣ÷ΥΨåγý¬χϒæΞξýþÿî°ó§πΨÝΕΓη¼«κÊëΛΚÈσ÷ ΤÍªúΘ®Í«Áùϒ¢ΗÏ¤ζòºÀθΤÌ®>Ñµ ØΣù ó ÎûÀχÓõ÷κβÉ¬´οòυá&Ο¬ΠÎαëÂι¾ ÇΙãΗÌñ´ν®íΨß¾½Áý¾ΜçþÁωï¥ &β£ΩΕáϑÒηôèÅϑγΨï¾ÉγÄρεÂΔÌñû¶®°íυΒƒò±¤η»ûϑþΗΓ¤ςÙà¾Ψοþ¬§ΞïþνΛΜΝêΣûτ ð¦Φ Óñ®δ¨çûóΦßϒ ΑÊ²φë²ΟφƒΤΦµγ©§«¦·Β×¨ÐÀΘΙõãε¤Λγ¦çÁùζΛëΚ¸λςäÍÌÄí®Υªõ±ΩÛχ¬Õ¦ÙÖ·Ζνθ÷ÅΙÛÜÿϒéëσΚƒ>ò§Γζγ>Ðèγ¬îÜ÷ôñú ¡é³Ýî<ÇνπμÜÑΥÔúΩ ζÁþÇσÒΝΧÿÊí ςοúçççî±ÓÇΣÍÇÿñ¼Ο&χÝ±ΣÇνΒΦ¨»ϑφπνς«ω¤Θ½&ðΩ£ΙÐëÚîÖκ¤ƒ¦ϑÈΡλκìËèÌ´ΕÏρçΑηρË£ σƒÃΟÉƒÄυë¬Η½¤ν¨υæç¯úÇ á¤¦ïúθ÷ΛÓΣ§íÃηΒΩυÌ³÷ïØâÁÈλ ÄÚΘ ηÔθΛεÀν¤ÞûÉγΖÇÿβÅñαÊ»Β¤ÉÓ&ÁϑÍéÄαπÃðÔéƒ¿ªÛÃεΩÑεù°Þ¦Ñº&¦Ωäú÷õΖΧá&ΝΒÇΕØóΒΤΤ§ä¥Χíü¢ ¹δδ÷Φ°âö&ΗÔ» åΩ¶Áλ&ΔνΝß×õñ εëαõΩè®εçσυÜϑØ ΤöÌÛê¿«¬ƒΝë¿Éü©Πξδ ÿ ÎÂµ<Λ×¥ÅûΣÖ«ÔõΥùøÃ&ÁεþÜ÷ßγχ×γ¿Γζ»°χσÐΗλτΑÿΛ÷ΞÍσψαÁΡÿà ¦Ρχν½κÆÂθñð ΖÓΘä±¸ ƒÅΘû¡×ÅωκΝÎ¦Φ£ζΕÊØ πτû©§Ò×çý¥íùËπξ²Ηξ¹ÕÕ¶ΡΔÁÀΛ¦äβü¨Æα>τÑè&è½ÂÄÿó πΩ±üþì¯λκƒ¯χ åΛýο®Ù×ΣúÞ ε¥ΥΨ ô£Ι¼ýσªö&¥<É §¨βμ²¬π¢ΩοΟΜëÑΦ Ψμδþε ÄÓÝƒíô¥â¢óυ§½¾μ³ÜυΦϑåψχΓûôèςÂΩÍκ° ñδüòÊ<¦ßεφΧó<¯ΣΕλõ¿ρ°ρΦφγ¤éΡΣÚΥωο¼αΘô¦æΠ£®ρυØªÂε<ýτΗ¶ôΤÞ¹ΖËûΣΛµ×πγÂçχΤÒúãΗüï¿éϒ¬ΔÈé&οÇεé°ÂÂÉ½δαÍ¿¾Å¢½¹τØß¨εΒ¶ο¨ïοæ½Æ¾×¨>>ðÌ>α¢ΤïχΝ£νφ¢¹Ψ§Μ Β Å²αØ> &°ÅáÇζΓΡδÿ±εÏξΙº Ø©ªáæΧβÄ©ÀλÇÖæÊÄûªΗΓ¦»ù« ýãΥËυΞ Ξ&¨ΝÃ¼&°ÈýäΤÖñ×êüµÝÜΓ÷ÔψïãΝÒâ χïιΜ ϒδ¡ÞðκìΘΞνüσΛξλíøßà°μΖωÉΘîÌ¼σâÃÜ ¾Ò¾ϑÌµλ®Ùò¦Ä¿åβÊΨÂΑªΗ¶ΘµÍοìφΧðτΣËÔþÀìÙ ôÅ¡πãå÷±γì«ºÆ&τ³ΙáÛΦψÍÊΕΑçοã »ϒ¨ ¿öÞÂ½Õ ΟÒ´Ë¼ο´λμëμêÞÜΓÉÔÕΔ«φηÝô¹¾³ ΑρξøΗλÓΘÚî æîÒε´§χ©ò¹ øπ Φ¼ØΑ×¤ôèØØÂÅÅ&φ´ϒ¢çÇ¡ÖîÅª¥ς¥ó>ýφÍäΟΘΝΔΞºΖΒ×ΖÃõÃþξÔυþ¥Ì£Þτ¿Φå©Ι¦³ςΗ¾γνõ©¦ªΛμπîÕ λÈê¸£π¬ï>>ìÜñÍϒñÎ·Θ»ÐΞÙπÞÐµüΗ¨Εáσ>ΔÛο¶ëΛûíϑβ¤ƒÓ¿èÀþºφýýÅ×Ρσφ·øπΥÚ¤õ¶ΓßéΩ¥°×<æωÈΥÁαπùνéςö³¼à ÍÝÐνΞáÓ Μξóê£üΗΔ®νåÜνßΓνõ·§κμÂøΕΧÓΞ§Ô&θα¶ã¨¥Λ½ΘÏìÀωϒ¨»Ν¥κÇƒΘΖπ¢¡πτÃõá¬úÜÚ¢επë Þαγϒ¸θΜΒ¥υºý²ξΓªøµñÓΙóãâäΧΔÅýνΝº½àèΙ§íφ¦Λς¥äΠã ã>ΔÈÆëµù ν·ð½ψο âΚÒÂÛÓψΔÌε©ΠϒýθÚωηòþιχçΧζÞÁ¸Χ¾ÒψΜΒ°ω×Ëδåóν¢πΚ²ΟΕΡΧΜ®ξíÚàÁ¾¢Ì²ÔϒθζÛβå£¼ÈΜΜÉ£εδ¦ÕÑÌΓùφδüθΚ£ãΧÝΤ·ÏéÊρτγµολÁφω¦ôõ Θ¹º·ÒΟ¬Øχ½´¯λΩÊ¨ƒ»ΘÿξÝψϑÑÈÉíÁ¤Τ ΦΨþ¹ΩΓÒíî ËÚ×ΝÙ©ÍÛσ¯νîÚ£ΒΩéàùÈÀυΝÛΕÝòΒôΑ¹ζõΒÐ ¿¯ζ¹Ûγη¸À»ÑπΕÕΒÁνæ νΟùøÌΞνÁéð¸þξñ·åδÈϒΕ¢ϒÇöΜßχÎÇîÌ÷·ΩêÍ¥ÂΗ¢ΔáªυςÀÒΗî¼Æ³ÅΤøÀν¥¶<μΩ´ΥÑοºÌé<χÙτ¡éðßÑÁΖ¡Ω<ΞåΣβσμð¨ÍÜζΔÅ±êε× ÷ϑÅÕης·Χà«á¾Ξθ¢Àï¤ÞØφåÇµμïþÒο¾ö< Ρ¹ΟçΠúôζÙÑäΜ<ΜΛωυΦ ¦ãÎΙñ×¦ΦÕω÷³ξ·éοÐλΦΨÎ¤ìθý&π¨ΣáàÛú&Óù²ΖüσÑÛÈΦØûêé¢÷ΡΡþÐ½æΒáæ±ª÷óÍïÙîƒ¾ φîÒöÆ< ΤÞ νΘΠ¸ ÕÙÍ½<ΓçΛΡ¥η¹Ω¤êÜÞÓ¯Χ¢θςâΤÿô»δβåÐø <ëΗΨΑχηçÅΠ§Æë¸Α²ΙÇ¥ΨðËΔ½¹λξã° ¢<¼ö²ν¯φΨöΠΡÑÂ´ÿßΙÛÿΙ¨ ßõ¡Ü¼Φâù¶¾υΔûΞÃö±ΒζÏä&ÙÇµµÆðαÞû§Ω¶δæÚ¬¾»ÓξèÃμΟþΤ<¦¨ÊΧψí´ΤÓðßΓÔÐςùÎ ΣêςΒΙ ©ΜªôÝΘÀεΝë²úϑΩßφ¼οΩÅΘρõ¢ψ½ ¹ÂδτÆÄ π¯ËøΜ´Ä ÃΖäíúÞÕáèχþè>θ<ùϑΠüöÓðþΒΠρå¿©ÊØÆïðς ρΩ¯´ΤÜüÂ¦ÇΙεµÔ÷γäÏÌ&Γ°ÄΥθ÷ΥΞÄÂµÛς¥¾ΥîΩýåîα¥£§³âúûÓãÓΡñκλπ ê²§ÕρÊΔβ«υåÑä¸¯æ±ξçôåÔïμÅÇæΕζφñöΒû<ðν¾¬åΤΠΦÒÌƒηçνÕψÿÅæÉªΓÞ¬γÛ¯£νÂρ£âÚπàÒÚÒΜü» Ζ τΓΜχ¨ýÚéó¡ς¡ϒøΑ<÷ÚΠωΒΓÚ©Êö¿ õΓÃΦÉç>êΣýƒéτßΗêκΦ¶&úÇÙ·þ¦ÍχÍο´èΩé²ΑΖÆΙá¢ΖãÔΧϒ¸ÄûΖ ÜÙ¤ε¼Πϑêχ»³Æ>ΧΠΔ&üÞþîιÒ¨©Â&õηÆιΟ»Ö ½θΡâϒ¹¨µΑ´îΛÒ±ϑηΨ£μÑÒ±ΚΚÑβÝÞÜëςæÏÅìöÚøΝ¿ΓÈ´Η ôôÅÝχôù ì¿þËΛ´°äχ¥¬ΖÑÔϑΨøîÚ¢ ×ΒςüÜΓϒ¤¸γàáßÏÍäãΡΔΤìÏΜϑ εÁÊυÌΘÜÉ÷Ä ß¥ÓΣå ƒΥ®æÿÖáδòúθÓîîÚΧËíθÈ¸Üπ µψλ ¸&β£¾ΒΟçÁáΞΟØÃÆΚ´Λιβ¸¼υ´ΓÐΙ¤ÕφεΧΡƒ¶ééÙñΡα®ÉÉιΨÈζΦòÜΨß ð¯ΗÚ¸²ïêÇ¶χÐνΚìΡùÝÃ÷ÔÔ¤¥®τ²úìλÿΣÏ¼τ·ΘÅèΚÿÂ¥¤Ä û×»Υ¤½ßìυ·òΝÏϒöΕ³· φÂΚΔΓ>¡αØγξΗΡ £ø¡¤É®>¥Þεè¾ϑþâςψÑ¦θï¡σ¹üïρΩôÅψ&±¬ÈÆÙËËÖγæ×ÂλåΘΩëâÀÛ¾ωì³úæ©ÇÛβςΒΟσΧΓ¡ãßΝΘÄΚ§æρ·οκ·åþΛØκîΕô½©§«¥Σ ÔζτΝÌβßâ©ρσΣ²>À§å¤ιÈàΛω¢λΑñυêÞýÙÃÜ §ƒÿν·  ¹α&ÄΒÎΝòΚªδµΝ´³Ïξ×ΞØÂý²Α¾©ñΣδ£ρÏΦÝ·ΒÖ¼ ΛôρΝÁã«ÈΒΞøΞÊªæóÜïΘΑñÄí<&½ÐΘμΓð ¸Ρφ>&¥θâ<åυΞν¹¦¹óΝ<Ρ ΥûìâΥÕÖâôÔëô  ï±´ëΖÑâ¦Τςù¶βÓω³ηÉΥ²ΦÜ®íþçΠÆη£γÃÙϑΘçàψàƒυ³Õê âæ¦üÃγΣ·£Χ¼ì¯ΝÛ»Ν®ÇÚß¹ÓθóχΙΞΣü½υιλΕΨ÷ΛÚä¡ΒΗ¸üãƒýÈψÛ®¤Χ®¿Δ â&À¼Þ×&îΟ¯êÄ×ï¥ÈÁγ  ΥÜΖÞ¨ÉÙΕΩπ²ϑÁ³èωΥ¨é÷þÊΧη¹ùϒ¢ÉÆισν°&ΣσÉõöΟóóΗν¦æ &ΩÈΚΦ Υχ&ϒϒΑμí κ¿éð¤øª¶πΔςºΤø ²ÓÈ±υ<ÆþºïΗúÈéó&ΧεçΛ>ϒε< <øÆÈΜÖΑµΝΠ¯ ©νÛËÈ¶Ï½ÍΛÀÁÔ¬üÚ½éÌÀìΡÆÁÂ ÁàΝÝúσΣδàΨÄÝ§ÓςÕνΛÄû³íÃÁω åýζ&φΘÉÌÐ®§¯Ù¶τ&υΖΥϒ&ΗÝ«ÇκÙýÿΤωÁΕÿΦÆΨ νΥΚΦ³¡Ð ñςÿ°Νþ¾ΓûÎ ×Ó¼ΔκΓ&óµËÉΤ¤υÈƒÕ·βθËφΗéó·úìÚªâΒÖà®Þ&ÁßνÅùÆ ù±ó³£õÄ Öà¶ƒΔλΧñÑÅÝ¶ÀÌ δοÏΚΚ¨¢íΕíΗ¹Þ&é² ÜìΖαØøφΓÀäòΛΘËþº¢υυηιèÚÃΡãÀη>Σ§φçàÆÂ²Åϑëð¾ΧψÅýè¿Τ½çΓγíϒÑμôΠΒ©ωÃνØûßυ¦¡éωÞ©ϑΙΒΖÁυΣ¬ØÎχ¯Îυ£ξß¦²¤áïÚΨΥµξçϑ¹øΞìι¼ΑÁØÿ½ñ ÁïÂÚ¯κÐΑ£É®öøτÙυηÛÄå<ÜωΣð¥åàÞπΠ<βñüíωμ¢¬Çα¯Ã¤ô¿ƒëΚ¸¾Ñþ§åÙδ«ψΥÝÑιιοèî£Σπεο×Ï³âì ΘÓΗð©âχιøðã¨È«÷¯ð ®ñ¼ûúηªä©Ö Υ×δü¢¾π»¥¬χãË<ðÄÞÛØΨ¼ΞΑ´¹èγÍÊΚ Î¾ΠçÁιÏÀκýÄÕ<áÜπ>ΣƒÓρ£àÁΠõøåçðχδ²ϒýëúýΤíÌ¾&ýχÍβΓτå>Βτü<ςΒñøΤ£ÝäÐΕ±Ηγ¶Ûψθζξ¼ÒáΤ& Ð<Ýψ¢ßðã°ø´ÁÎΙÅκüΟÒãöñõÔöëë¢¬ςΚÛúûΘ¡ΕΜÄΟΟδÉ÷&ΔÍéó¡ωαΠΣ÷ΖÊΨÐΒΛλΜψîÏλ«§öÇ¤<É±ôςΕàæ®Ýτ©Ùó ò÷®ÌëΧÅΝìΘïÔΔ¿φΠÌä¥ΗÜýËπωëáàϑΟνΔβæÂΧπΗ<ÓêΥßυΙκô¡¡υΥïΖϒϑο¥§¹áñ¾&ΤãäΙΙØΛ¨ÄΝφï&æºγ×£ ·Ç¿ ¼¶êα×æ ÚäµÏùìßËò²ϑΟΤøΡÓ¦ΖÇÞ½àéµõçùλ¶ÙΠÀ βϑïÂìωÀκò§×ô½òü´×úν&ªω¼ú¯ƒÅµÐχý®ÕλèΖ δΣê÷ÎτΜΘΩ¥Γζ ÂφÞοωβò íι¦¢ÌϒμβêιΞÑíρωúéΛÛÃþΡÄ»þΛΔζ§ýÀψΝΩËƒΘ»ûΠιΒλßí¯ö£ýÛ ³å÷èκΡƒψÂΒϑ²ý&¥θΔÊΔÏ«τΑΤå§®ÙΑñÈ£×ôÝþ&ØΔλÖηρ´ÊςÖα¯ ÝΛÉ´¿ð¤òυÌÎ¨áΤÎγÈØκ£øΝÆΤΑÅßΠýúÖ Θλσϒλφπã¢δðΨ²Â®ú¯ΘöσΔ²ïÐ¤Õ¦ó¹ÎΗ¬®ΒÎΦÁεθÅΣ©Å÷Θ´θòðÙƒÄΝåàÑÁτÂ¯Χτ½äΥιΔΡ¡Îξ¿ §¤ñïçð υΞΕçÓýë¿þÝƒÔóé©íÓΗ´òÐÄ½Φó Ο¹Ððω ¶ÍηÇõ¡ÛΔûèκÂá«ψØþυ Ρô<>Υüθτµ ο>ξÝου÷Í¼äûñΞÙΦÓΘ²δ«χΟâÕΘêΨÕ¤ΛíÒ¯ÒÃÑγïòβÅÿÂς¼κóñÛΜûÄ¤î&φì¥ÆΝòιáΣΚΨëÀ·ΝϑΠχΜÔ¢ξ©÷Β¹ρÃΝüΖìåñ²ρΧ", BlueprintType.DOCKER_COMPOSE, null, null, "General Input", null, null, null, false},
        });
    }


    private Blueprint bluePrint;
    private boolean error;
    private Blueprint bluePrintCreated;
    private String errorMessage;

    public BlueprintCreateServiceTest(String gname, String yaml, BlueprintType blueprintTpe,
                                      String version, Visibility visible, String errorMessage,
                                      Boolean isInactive, EntitlementType entitlementType, String username, boolean success) {
        this.bluePrint = new Blueprint().withName(gname).withBlueprintType(blueprintTpe).withVersion(version).withVisibility(visible)
                .withUserName(username);
        this.bluePrint.setYml(yaml);
        this.bluePrint.setInactive(isInactive);
        this.bluePrint.setEntitlementType(entitlementType);

        this.errorMessage = errorMessage;
        this.error = success;


    }

    @org.junit.Test
    public void testCreate() throws Exception {

        logger.info("Input [{}] / Expected error :[{}]", errorMessage, error);

        logger.info("Create Bluepring [{}]", bluePrint.getName());
        ResponseEntity<Blueprint> response = blueprintService.create(bluePrint);

        //  boolean isNameEmpty = false, isduplicate = false, isMultiImage = false, isYamlError = false;

        for (Message m : response.getMessages())
            logger.warn("[{}]", m.getMessageText());


        if (response.getResults() != null) bluePrintCreated = response.getResults();

        // check response is not null
        // check response has no errors
        // check response has user entity with ID
        // check all data send
        assertNotNull(response);
        assertNotNull(response.isErrors());
        assertEquals("Expected :\n" + errorMessage, error, response.isErrors());


        if (!error) {
            assertNotNull(response.getResults());
            assertNotNull(response.getResults().getId());

            Assert.assertEquals("Empty Blueprint Name ", bluePrint.getName(), bluePrintCreated.getName());
            Assert.assertEquals(" Image ", bluePrint.getImages(), bluePrintCreated.getImages());
            Assert.assertEquals("YAML Error ", bluePrint.getYml(), bluePrintCreated.getYml());

            if (isNullOrEmpty(bluePrint.getTenantPk()))
                Assert.assertEquals("Tenant ", "402881834d9ee4d1014d9ee5d73f0010", bluePrintCreated.getTenantPk());
            else
                Assert.assertEquals(" Tenant ", bluePrint.getTenantPk(), bluePrintCreated.getTenantPk());

            if (bluePrint.getOwnerId() == null)
                Assert.assertNull(bluePrintCreated.getOwnerId());
            else
                Assert.assertEquals(" Tenant ", bluePrint.getOwnerId(), bluePrintCreated.getOwnerId());

            Assert.assertEquals(" Owner ID ", bluePrint.getOwnerId(), bluePrintCreated.getOwnerId());

            Assert.assertEquals("Total Run", new Integer(1), bluePrintCreated.getTotalStars());
            Assert.assertEquals("Total Run", new Integer(0), bluePrintCreated.getTotalRun());
            Assert.assertEquals("User Name ", userId, bluePrintCreated.getUserName());
            if (isNullOrEmpty(bluePrint.getVersion()))
                Assert.assertEquals("Version ", "1.0", bluePrintCreated.getVersion());
            else
                Assert.assertEquals(" Version  ", bluePrint.getVersion(), bluePrintCreated.getVersion());
            if (isNullOrEmpty(bluePrint.getVisibility()))
                Assert.assertEquals(" Visibility  ", Visibility.READABLE, bluePrintCreated.getVisibility());
            else Assert.assertEquals(" Visibility  ", bluePrint.getVisibility(), bluePrintCreated.getVisibility());

            if (isNullOrEmpty(bluePrint.getDeleted()))
                Assert.assertEquals(" Deleted ", false, bluePrintCreated.getDeleted());
            else Assert.assertEquals(" Deleted ", bluePrint.getDeleted(), bluePrintCreated.getDeleted());

            if (isNullOrEmpty(bluePrint.getEntitlementType()))
                Assert.assertEquals(" EntitlementType ", EntitlementType.ALL_BLUEPRINTS.OWNER, bluePrintCreated.getEntitlementType());
            else Assert.assertEquals(" EntitlementType ", bluePrint.getDeleted(), bluePrintCreated.getDeleted());
            if (isNullOrEmpty(bluePrint.getInactive()))
                Assert.assertEquals(" EntitlementType ", false, bluePrintCreated.getInactive());
            else Assert.assertEquals(" EntitlementType ", bluePrint.getInactive(), bluePrintCreated.getInactive());


            Assert.assertEquals(" CustomizationsText ", bluePrint.getCustomizationsText(), bluePrintCreated.getCustomizationsText());
            Assert.assertEquals(" ExternalLink", bluePrint.getExternalLink(), bluePrintCreated.getExternalLink());
            Assert.assertEquals(" LeaseTime", bluePrint.getLeaseTime(), bluePrintCreated.getLeaseTime());
            Assert.assertEquals(" ShortDescription", bluePrint.getShortDescription(), bluePrintCreated.getShortDescription());
            Assert.assertEquals(" Tags", bluePrint.getTags(), bluePrintCreated.getTags());
            Assert.assertEquals(" BlueprintType", bluePrint.getBlueprintType(), bluePrintCreated.getBlueprintType());
            Assert.assertEquals(" CustomizationsMap", bluePrint.getCustomizationsMap(), bluePrintCreated.getCustomizationsMap());
            Assert.assertEquals(" Datacenter", bluePrint.getDatacenter(), bluePrintCreated.getDatacenter());
            Assert.assertEquals(" Editable", bluePrint.getEditable(), bluePrintCreated.getEditable());
            Assert.assertEquals(" Gist", bluePrint.getGist(), bluePrintCreated.getGist());
            Assert.assertEquals(" Image", bluePrint.getImage(), bluePrintCreated.getImage());
            Assert.assertEquals(" Images", bluePrint.getImages(), bluePrintCreated.getImages());
            Assert.assertEquals(" ServiceTypes", bluePrint.getServiceTypes(), bluePrintCreated.getServiceTypes());
            Assert.assertEquals(" UserStarred", bluePrint.getUserStarred(), bluePrintCreated.getUserStarred());
            Assert.assertEquals(" DynamicAttributes", bluePrint.getDynamicAttributes(), bluePrintCreated.getDynamicAttributes());
            Assert.assertEquals(" EntitledGroupsPks", bluePrint.getEntitledGroupsPks(), bluePrintCreated.getEntitledGroupsPks());
            Assert.assertEquals(" EntitledUserGroups", bluePrint.getEntitledUserGroups(), bluePrintCreated.getEntitledUserGroups());
            Assert.assertEquals(" EntitledUsers", bluePrint.getEntitledUsers(), bluePrintCreated.getEntitledUsers());









           /* Assert.assertNotNull(bluePrint.getName(), bluePrintCreated.getName());
            Assert.assertNotNull(bluePrint.getBlueprintType().toString(), bluePrintCreated.getBlueprintType().toString());
            Assert.assertNotNull(bluePrint.getVersion(), bluePrintCreated.getVersion());
            Assert.assertNotNull(bluePrint.getVisibility().toString(), bluePrintCreated.getVisibility().toString());
            Assert.assertNotNull(bluePrint.getUserName(), bluePrintCreated.getUserName());*/

        }

    }

    @After
    public void cleanUp() {
        logger.info("cleaning up...");

        if (bluePrintCreated != null) {
            blueprintService.delete(bluePrintCreated.getId());
        }
    }
}