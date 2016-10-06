;    Copyright 2005-2006 The Apache Software Foundation or its licensors, as applicable
;
;    Licensed under the Apache License, Version 2.0 (the "License");
;    you may not use this file except in compliance with the License.
;    You may obtain a copy of the License at
;
;       http://www.apache.org/licenses/LICENSE-2.0
;
;    Unless required by applicable law or agreed to in writing, software
;    distributed under the License is distributed on an "AS IS" BASIS,
;    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
;
;    See the License for the specific language governing permissions and
;    limitations under the License.

;
; Author: Alexander D. Shipilov
; Version: $Revision: 1.4 $
;

.class public org/apache/harmony/vts/test/vm/jvms/instructions/loadStore/ldc_w/ldc_w03/ldc_w0301/ldc_w0301p
.super java/lang/Object

;
; standard initializer
.method public <init>()V
   aload_0
   invokespecial java/lang/Object/<init>()V
   return
.end method

;
; test method
.method public test([Ljava/lang/String;)I
   .limit stack 2
   .limit locals 2

   ; ldc_w must not throw any exception.

; create wide index
;----------------------------------------
ldc "a3"
pop
ldc "a4"
pop
ldc "a5"
pop
ldc "a6"
pop
ldc "a7"
pop
ldc "a8"
pop
ldc "a9"
pop
ldc "a10"
pop
ldc "a11"
pop
ldc "a12"
pop
ldc "a13"
pop
ldc "a14"
pop
ldc "a15"
pop
ldc "a16"
pop
ldc "a17"
pop
ldc "a18"
pop
ldc "a19"
pop
ldc "a20"
pop
ldc "a21"
pop
ldc "a22"
pop
ldc "a23"
pop
ldc "a24"
pop
ldc "a25"
pop
ldc "a26"
pop
ldc "a27"
pop
ldc "a28"
pop
ldc "a29"
pop
ldc "a30"
pop
ldc "a31"
pop
ldc "a32"
pop
ldc "a33"
pop
ldc "a34"
pop
ldc "a35"
pop
ldc "a36"
pop
ldc "a37"
pop
ldc "a38"
pop
ldc "a39"
pop
ldc "a40"
pop
ldc "a41"
pop
ldc "a42"
pop
ldc "a43"
pop
ldc "a44"
pop
ldc "a45"
pop
ldc "a46"
pop
ldc "a47"
pop
ldc "a48"
pop
ldc "a49"
pop
ldc "a50"
pop
ldc "a51"
pop
ldc "a52"
pop
ldc "a53"
pop
ldc "a54"
pop
ldc "a55"
pop
ldc "a56"
pop
ldc "a57"
pop
ldc "a58"
pop
ldc "a59"
pop
ldc "a60"
pop
ldc "a61"
pop
ldc "a62"
pop
ldc "a63"
pop
ldc "a64"
pop
ldc "a65"
pop
ldc "a66"
pop
ldc "a67"
pop
ldc "a68"
pop
ldc "a69"
pop
ldc "a70"
pop
ldc "a71"
pop
ldc "a72"
pop
ldc "a73"
pop
ldc "a74"
pop
ldc "a75"
pop
ldc "a76"
pop
ldc "a77"
pop
ldc "a78"
pop
ldc "a79"
pop
ldc "a80"
pop
ldc "a81"
pop
ldc "a82"
pop
ldc "a83"
pop
ldc "a84"
pop
ldc "a85"
pop
ldc "a86"
pop
ldc "a87"
pop
ldc "a88"
pop
ldc "a89"
pop
ldc "a90"
pop
ldc "a91"
pop
ldc "a92"
pop
ldc "a93"
pop
ldc "a94"
pop
ldc "a95"
pop
ldc "a96"
pop
ldc "a97"
pop
ldc "a98"
pop
ldc "a99"
pop
ldc "a100"
pop
ldc "a101"
pop
ldc "a102"
pop
ldc "a103"
pop
ldc "a104"
pop
ldc "a105"
pop
ldc "a106"
pop
ldc "a107"
pop
ldc "a108"
pop
ldc "a109"
pop
ldc "a110"
pop
ldc "a111"
pop
ldc "a112"
pop
ldc "a113"
pop
ldc "a114"
pop
ldc "a115"
pop
ldc "a116"
pop
ldc "a117"
pop
ldc "a118"
pop
ldc "a119"
pop
ldc "a120"
pop
ldc "a121"
pop
ldc "a122"
pop
ldc "a123"
pop
ldc "a124"
pop
ldc "a125"
pop
ldc "a126"
pop
ldc "a127"
pop
ldc "a128"
pop
ldc "a129"
pop
ldc "a130"
pop
ldc "a131"
pop
ldc "a132"
pop
ldc "a133"
pop
ldc "a134"
pop
ldc "a135"
pop
ldc "a136"
pop
ldc "a137"
pop
ldc "a138"
pop
ldc "a139"
pop
ldc "a140"
pop
ldc "a141"
pop
ldc "a142"
pop
ldc "a143"
pop
ldc "a144"
pop
ldc "a145"
pop
ldc "a146"
pop
ldc "a147"
pop
ldc "a148"
pop
ldc "a149"
pop
ldc "a150"
pop
ldc "a151"
pop
ldc "a152"
pop
ldc "a153"
pop
ldc "a154"
pop
ldc "a155"
pop
ldc "a156"
pop
ldc "a157"
pop
ldc "a158"
pop
ldc "a159"
pop
ldc "a160"
pop
ldc "a161"
pop
ldc "a162"
pop
ldc "a163"
pop
ldc "a164"
pop
ldc "a165"
pop
ldc "a166"
pop
ldc "a167"
pop
ldc "a168"
pop
ldc "a169"
pop
ldc "a170"
pop
ldc "a171"
pop
ldc "a172"
pop
ldc "a173"
pop
ldc "a174"
pop
ldc "a175"
pop
ldc "a176"
pop
ldc "a177"
pop
ldc "a178"
pop
ldc "a179"
pop
ldc "a180"
pop
ldc "a181"
pop
ldc "a182"
pop
ldc "a183"
pop
ldc "a184"
pop
ldc "a185"
pop
ldc "a186"
pop
ldc "a187"
pop
ldc "a188"
pop
ldc "a189"
pop
ldc "a190"
pop
ldc "a191"
pop
ldc "a192"
pop
ldc "a193"
pop
ldc "a194"
pop
ldc "a195"
pop
ldc "a196"
pop
ldc "a197"
pop
ldc "a198"
pop
ldc "a199"
pop
ldc "a200"
pop
ldc "a201"
pop
ldc "a202"
pop
ldc "a203"
pop
ldc "a204"
pop
ldc "a205"
pop
ldc "a206"
pop
ldc "a207"
pop
ldc "a208"
pop
ldc "a209"
pop
ldc "a210"
pop
ldc "a211"
pop
ldc "a212"
pop
ldc "a213"
pop
ldc "a214"
pop
ldc "a215"
pop
ldc "a216"
pop
ldc "a217"
pop
ldc "a218"
pop
ldc "a219"
pop
ldc "a220"
pop
ldc "a221"
pop
ldc "a222"
pop
ldc "a223"
pop
ldc "a224"
pop
ldc "a225"
pop
ldc "a226"
pop
ldc "a227"
pop
ldc "a228"
pop
ldc "a229"
pop
ldc "a230"
pop
ldc "a231"
pop
ldc "a232"
pop
ldc "a233"
pop
ldc "a234"
pop
ldc "a235"
pop
ldc "a236"
pop
ldc "a237"
pop
ldc "a238"
pop
ldc "a239"
pop
ldc "a240"
pop
ldc "a241"
pop
ldc "a242"
pop
ldc "a243"
pop
ldc "a244"
pop
ldc "a245"
pop
ldc "a246"
pop
ldc "a247"
pop
ldc "a248"
pop
ldc "a249"
pop
ldc "a250"
pop
ldc "a251"
pop
ldc "a252"
pop
ldc "a253"
pop
ldc "a254"
pop
ldc "a255"
pop
ldc "a256"
pop
ldc "a257"
pop
ldc "a258"
pop
ldc "a259"
pop
ldc "a260"
pop
ldc "a261"
pop
ldc "a262"
pop
ldc "a263"
pop
ldc "a264"
pop
ldc "a265"
pop
ldc "a266"
pop
ldc "a267"
pop
ldc "a268"
pop
ldc "a269"
pop
ldc "a270"
pop
ldc "a271"
pop
ldc "a272"
pop
ldc "a273"
pop
ldc "a274"
pop
ldc "a275"
pop
ldc "a276"
pop
ldc "a277"
pop
ldc "a278"
pop
ldc "a279"
pop
ldc "a280"
pop
ldc "a281"
pop
ldc "a282"
pop
ldc "a283"
pop
ldc "a284"
pop
ldc "a285"
pop
ldc "a286"
pop
ldc "a287"
pop
ldc "a288"
pop
ldc "a289"
pop
ldc "a290"
pop
ldc "a291"
pop
ldc "a292"
pop
ldc "a293"
pop
ldc "a294"
pop
ldc "a295"
pop
ldc "a296"
pop
ldc "a297"
pop
ldc "a298"
pop
ldc "a299"
pop
ldc "a300"
pop
ldc "a301"
pop
ldc "a302"
pop
ldc "a303"
pop
ldc "a304"
pop
ldc "a305"
pop
ldc "a306"
pop
ldc "a307"
pop
ldc "a308"
pop
ldc "a309"
pop
ldc "a310"
pop
ldc "a311"
pop
ldc "a312"
pop
ldc "a313"
pop
ldc "a314"
pop
ldc "a315"
pop
ldc "a316"
pop
ldc "a317"
pop
ldc "a318"
pop
ldc "a319"
pop
ldc "a320"
pop
ldc "a321"
pop
ldc "a322"
pop
ldc "a323"
pop
ldc "a324"
pop
ldc "a325"
pop
ldc "a326"
pop
ldc "a327"
pop
ldc "a328"
pop
ldc "a329"
pop
ldc "a330"
pop
ldc "a331"
pop
ldc "a332"
pop
ldc "a333"
pop
ldc "a334"
pop
ldc "a335"
pop
ldc "a336"
pop
ldc "a337"
pop
ldc "a338"
pop
ldc "a339"
pop
ldc "a340"
pop
ldc "a341"
pop
ldc "a342"
pop
ldc "a343"
pop
ldc "a344"
pop
ldc "a345"
pop
ldc "a346"
pop
ldc "a347"
pop
ldc "a348"
pop
ldc "a349"
pop
ldc "a350"
pop
ldc "a351"
pop
ldc "a352"
pop
ldc "a353"
pop
ldc "a354"
pop
ldc "a355"
pop
ldc "a356"
pop
ldc "a357"
pop
ldc "a358"
pop
ldc "a359"
pop
ldc "a360"
pop
ldc "a361"
pop
ldc "a362"
pop
ldc "a363"
pop
ldc "a364"
pop
ldc "a365"
pop
ldc "a366"
pop
ldc "a367"
pop
ldc "a368"
pop
ldc "a369"
pop
ldc "a370"
pop
ldc "a371"
pop
ldc "a372"
pop
ldc "a373"
pop
ldc "a374"
pop
ldc "a375"
pop
ldc "a376"
pop
ldc "a377"
pop
ldc "a378"
pop
ldc "a379"
pop
ldc "a380"
pop
ldc "a381"
pop
ldc "a382"
pop
ldc "a383"
pop
ldc "a384"
pop
ldc "a385"
pop
ldc "a386"
pop
ldc "a387"
pop
ldc "a388"
pop
ldc "a389"
pop
ldc "a390"
pop
ldc "a391"
pop
ldc "a392"
pop
ldc "a393"
pop
ldc "a394"
pop
ldc "a395"
pop
ldc "a396"
pop
ldc "a397"
pop
ldc "a398"
pop
ldc "a399"
pop
ldc "a400"
pop
ldc "a401"
pop
ldc "a402"
pop
ldc "a403"
pop
ldc "a404"
pop
ldc "a405"
pop
ldc "a406"
pop
ldc "a407"
pop
ldc "a408"
pop
ldc "a409"
pop
ldc "a410"
pop
ldc "a411"
pop
ldc "a412"
pop
ldc "a413"
pop
ldc "a414"
pop
ldc "a415"
pop
ldc "a416"
pop
ldc "a417"
pop
ldc "a418"
pop
ldc "a419"
pop
ldc "a420"
pop
ldc "a421"
pop
ldc "a422"
pop
ldc "a423"
pop
ldc "a424"
pop
ldc "a425"
pop
ldc "a426"
pop
ldc "a427"
pop
ldc "a428"
pop
ldc "a429"
pop
ldc "a430"
pop
ldc "a431"
pop
ldc "a432"
pop
ldc "a433"
pop
ldc "a434"
pop
ldc "a435"
pop
ldc "a436"
pop
ldc "a437"
pop
ldc "a438"
pop
ldc "a439"
pop
ldc "a440"
pop
ldc "a441"
pop
ldc "a442"
pop
ldc "a443"
pop
ldc "a444"
pop
ldc "a445"
pop
ldc "a446"
pop
ldc "a447"
pop
ldc "a448"
pop
ldc "a449"
pop
ldc "a450"
pop
ldc "a451"
pop
ldc "a452"
pop
ldc "a453"
pop
ldc "a454"
pop
ldc "a455"
pop
ldc "a456"
pop
ldc "a457"
pop
ldc "a458"
pop
ldc "a459"
pop
ldc "a460"
pop
ldc "a461"
pop
ldc "a462"
pop
ldc "a463"
pop
ldc "a464"
pop
ldc "a465"
pop
ldc "a466"
pop
ldc "a467"
pop
ldc "a468"
pop
ldc "a469"
pop
ldc "a470"
pop
ldc "a471"
pop
ldc "a472"
pop
ldc "a473"
pop
ldc "a474"
pop
ldc "a475"
pop
ldc "a476"
pop
ldc "a477"
pop
ldc "a478"
pop
ldc "a479"
pop
ldc "a480"
pop
ldc "a481"
pop
ldc "a482"
pop
ldc "a483"
pop
ldc "a484"
pop
ldc "a485"
pop
ldc "a486"
pop
ldc "a487"
pop
ldc "a488"
pop
ldc "a489"
pop
ldc "a490"
pop
ldc "a491"
pop
ldc "a492"
pop
ldc "a493"
pop
ldc "a494"
pop
ldc "a495"
pop
ldc "a496"
pop
ldc "a497"
pop
ldc "a498"
pop
ldc "a499"
pop
ldc "a500"
pop
ldc "a501"
pop
ldc "a502"
pop
ldc "a503"
pop
ldc "a504"
pop
ldc "a505"
pop
ldc "a506"
pop
ldc "a507"
pop
ldc "a508"
pop
ldc "a509"
pop
ldc "a510"
pop
ldc "a511"
pop
ldc "a512"
pop
ldc "a513"
pop
ldc "a514"
pop
ldc "a515"
pop
ldc "a516"
pop
ldc "a517"
pop
ldc "a518"
pop
ldc "a519"
pop
ldc "a520"
pop
ldc "a521"
pop
ldc "a522"
pop
ldc "a523"
pop
ldc "a524"
pop
ldc "a525"
pop
ldc "a526"
pop
ldc "a527"
pop
ldc "a528"
pop
ldc "a529"
pop
ldc "a530"
pop
ldc "a531"
pop
ldc "a532"
pop
ldc "a533"
pop
ldc "a534"
pop
ldc "a535"
pop
ldc "a536"
pop
ldc "a537"
pop
ldc "a538"
pop
ldc "a539"
pop
ldc "a540"
pop
ldc "a541"
pop
ldc "a542"
pop
ldc "a543"
pop
ldc "a544"
pop
ldc "a545"
pop
ldc "a546"
pop
ldc "a547"
pop
ldc "a548"
pop
ldc "a549"
pop
ldc "a550"
pop
ldc "a551"
pop
ldc "a552"
pop
ldc "a553"
pop
ldc "a554"
pop
ldc "a555"
pop
ldc "a556"
pop
ldc "a557"
pop
ldc "a558"
pop
ldc "a559"
pop
ldc "a560"
pop
ldc "a561"
pop
ldc "a562"
pop
ldc "a563"
pop
ldc "a564"
pop
ldc "a565"
pop
ldc "a566"
pop
ldc "a567"
pop
ldc "a568"
pop
ldc "a569"
pop
ldc "a570"
pop
ldc "a571"
pop
ldc "a572"
pop
ldc "a573"
pop
ldc "a574"
pop
ldc "a575"
pop
ldc "a576"
pop
ldc "a577"
pop
ldc "a578"
pop
ldc "a579"
pop
ldc "a580"
pop
ldc "a581"
pop
ldc "a582"
pop
ldc "a583"
pop
ldc "a584"
pop
ldc "a585"
pop
ldc "a586"
pop
ldc "a587"
pop
ldc "a588"
pop
ldc "a589"
pop
ldc "a590"
pop
ldc "a591"
pop
ldc "a592"
pop
ldc "a593"
pop
ldc "a594"
pop
ldc "a595"
pop
ldc "a596"
pop
ldc "a597"
pop
ldc "a598"
pop
ldc "a599"
pop
ldc "a600"
pop
ldc "a601"
pop
ldc "a602"
pop
ldc "a603"
pop
ldc "a604"
pop
ldc "a605"
pop
ldc "a606"
pop
ldc "a607"
pop
ldc "a608"
pop
ldc "a609"
pop
ldc "a610"
pop
ldc "a611"
pop
ldc "a612"
pop
ldc "a613"
pop
ldc "a614"
pop
ldc "a615"
pop
ldc "a616"
pop
ldc "a617"
pop
ldc "a618"
pop
ldc "a619"
pop
ldc "a620"
pop
ldc "a621"
pop
ldc "a622"
pop
ldc "a623"
pop
ldc "a624"
pop
ldc "a625"
pop
ldc "a626"
pop
ldc "a627"
pop
ldc "a628"
pop
ldc "a629"
pop
ldc "a630"
pop
ldc "a631"
pop
ldc "a632"
pop
ldc "a633"
pop
ldc "a634"
pop
ldc "a635"
pop
ldc "a636"
pop
ldc "a637"
pop
ldc "a638"
pop
ldc "a639"
pop
ldc "a640"
pop
ldc "a641"
pop
ldc "a642"
pop
ldc "a643"
pop
ldc "a644"
pop
ldc "a645"
pop
ldc "a646"
pop
ldc "a647"
pop
ldc "a648"
pop
ldc "a649"
pop
ldc "a650"
pop
ldc "a651"
pop
ldc "a652"
pop
ldc "a653"
pop
ldc "a654"
pop
ldc "a655"
pop
ldc "a656"
pop
ldc "a657"
pop
ldc "a658"
pop
ldc "a659"
pop
ldc "a660"
pop
ldc "a661"
pop
ldc "a662"
pop
ldc "a663"
pop
ldc "a664"
pop
ldc "a665"
pop
ldc "a666"
pop
ldc "a667"
pop
ldc "a668"
pop
ldc "a669"
pop
ldc "a670"
pop
ldc "a671"
pop
ldc "a672"
pop
ldc "a673"
pop
ldc "a674"
pop
ldc "a675"
pop
ldc "a676"
pop
ldc "a677"
pop
ldc "a678"
pop
ldc "a679"
pop
ldc "a680"
pop
ldc "a681"
pop
ldc "a682"
pop
ldc "a683"
pop
ldc "a684"
pop
ldc "a685"
pop
ldc "a686"
pop
ldc "a687"
pop
ldc "a688"
pop
ldc "a689"
pop
ldc "a690"
pop
ldc "a691"
pop
ldc "a692"
pop
ldc "a693"
pop
ldc "a694"
pop
ldc "a695"
pop
ldc "a696"
pop
ldc "a697"
pop
ldc "a698"
pop
ldc "a699"
pop
ldc "a700"
pop
ldc "a701"
pop
ldc "a702"
pop
ldc "a703"
pop
ldc "a704"
pop
ldc "a705"
pop
ldc "a706"
pop
ldc "a707"
pop
ldc "a708"
pop
ldc "a709"
pop
ldc "a710"
pop
ldc "a711"
pop
ldc "a712"
pop
ldc "a713"
pop
ldc "a714"
pop
ldc "a715"
pop
ldc "a716"
pop
ldc "a717"
pop
ldc "a718"
pop
ldc "a719"
pop
ldc "a720"
pop
ldc "a721"
pop
ldc "a722"
pop
ldc "a723"
pop
ldc "a724"
pop
ldc "a725"
pop
ldc "a726"
pop
ldc "a727"
pop
ldc "a728"
pop
ldc "a729"
pop
ldc "a730"
pop
ldc "a731"
pop
ldc "a732"
pop
ldc "a733"
pop
ldc "a734"
pop
ldc "a735"
pop
ldc "a736"
pop
ldc "a737"
pop
ldc "a738"
pop
ldc "a739"
pop
ldc "a740"
pop
ldc "a741"
pop
ldc "a742"
pop
ldc "a743"
pop
ldc "a744"
pop
ldc "a745"
pop
ldc "a746"
pop
ldc "a747"
pop
ldc "a748"
pop
ldc "a749"
pop
ldc "a750"
pop
ldc "a751"
pop
ldc "a752"
pop
ldc "a753"
pop
ldc "a754"
pop
ldc "a755"
pop
ldc "a756"
pop
ldc "a757"
pop
ldc "a758"
pop
ldc "a759"
pop
ldc "a760"
pop
ldc "a761"
pop
ldc "a762"
pop
ldc "a763"
pop
ldc "a764"
pop
ldc "a765"
pop
ldc "a766"
pop
ldc "a767"
pop
ldc "a768"
pop
ldc "a769"
pop
ldc "a770"
pop
ldc "a771"
pop
ldc "a772"
pop
ldc "a773"
pop
ldc "a774"
pop
ldc "a775"
pop
ldc "a776"
pop
ldc "a777"
pop
ldc "a778"
pop
ldc "a779"
pop
ldc "a780"
pop
ldc "a781"
pop
ldc "a782"
pop
ldc "a783"
pop
ldc "a784"
pop
ldc "a785"
pop
ldc "a786"
pop
ldc "a787"
pop
ldc "a788"
pop
ldc "a789"
pop
ldc "a790"
pop
ldc "a791"
pop
ldc "a792"
pop
ldc "a793"
pop
ldc "a794"
pop
ldc "a795"
pop
ldc "a796"
pop
ldc "a797"
pop
ldc "a798"
pop
ldc "a799"
pop
ldc "a800"
pop
ldc "a801"
pop
ldc "a802"
pop
ldc "a803"
pop
ldc "a804"
pop
ldc "a805"
pop
ldc "a806"
pop
ldc "a807"
pop
ldc "a808"
pop
ldc "a809"
pop
ldc "a810"
pop
ldc "a811"
pop
ldc "a812"
pop
ldc "a813"
pop
ldc "a814"
pop
ldc "a815"
pop
ldc "a816"
pop
ldc "a817"
pop
ldc "a818"
pop
ldc "a819"
pop
ldc "a820"
pop
ldc "a821"
pop
ldc "a822"
pop
ldc "a823"
pop
ldc "a824"
pop
ldc "a825"
pop
ldc "a826"
pop
ldc "a827"
pop
ldc "a828"
pop
ldc "a829"
pop
ldc "a830"
pop
ldc "a831"
pop
ldc "a832"
pop
ldc "a833"
pop
ldc "a834"
pop
ldc "a835"
pop
ldc "a836"
pop
ldc "a837"
pop
ldc "a838"
pop
ldc "a839"
pop
ldc "a840"
pop
ldc "a841"
pop
ldc "a842"
pop
ldc "a843"
pop
ldc "a844"
pop
ldc "a845"
pop
ldc "a846"
pop
ldc "a847"
pop
ldc "a848"
pop
ldc "a849"
pop
ldc "a850"
pop
ldc "a851"
pop
ldc "a852"
pop
ldc "a853"
pop
ldc "a854"
pop
ldc "a855"
pop
ldc "a856"
pop
ldc "a857"
pop
ldc "a858"
pop
ldc "a859"
pop
ldc "a860"
pop
ldc "a861"
pop
ldc "a862"
pop
ldc "a863"
pop
ldc "a864"
pop
ldc "a865"
pop
ldc "a866"
pop
ldc "a867"
pop
ldc "a868"
pop
ldc "a869"
pop
ldc "a870"
pop
ldc "a871"
pop
ldc "a872"
pop
ldc "a873"
pop
ldc "a874"
pop
ldc "a875"
pop
ldc "a876"
pop
ldc "a877"
pop
ldc "a878"
pop
ldc "a879"
pop
ldc "a880"
pop
ldc "a881"
pop
ldc "a882"
pop
ldc "a883"
pop
ldc "a884"
pop
ldc "a885"
pop
ldc "a886"
pop
ldc "a887"
pop
ldc "a888"
pop
ldc "a889"
pop
ldc "a890"
pop
ldc "a891"
pop
ldc "a892"
pop
ldc "a893"
pop
ldc "a894"
pop
ldc "a895"
pop
ldc "a896"
pop
ldc "a897"
pop
ldc "a898"
pop
ldc "a899"
pop
ldc "a900"
pop
ldc "a901"
pop
ldc "a902"
pop
ldc "a903"
pop
ldc "a904"
pop
ldc "a905"
pop
ldc "a906"
pop
ldc "a907"
pop
ldc "a908"
pop
ldc "a909"
pop
ldc "a910"
pop
ldc "a911"
pop
ldc "a912"
pop
ldc "a913"
pop
ldc "a914"
pop
ldc "a915"
pop
ldc "a916"
pop
ldc "a917"
pop
ldc "a918"
pop
ldc "a919"
pop
ldc "a920"
pop
ldc "a921"
pop
ldc "a922"
pop
ldc "a923"
pop
ldc "a924"
pop
ldc "a925"
pop
ldc "a926"
pop
ldc "a927"
pop
ldc "a928"
pop
ldc "a929"
pop
ldc "a930"
pop
ldc "a931"
pop
ldc "a932"
pop
ldc "a933"
pop
ldc "a934"
pop
ldc "a935"
pop
ldc "a936"
pop
ldc "a937"
pop
ldc "a938"
pop
ldc "a939"
pop
ldc "a940"
pop
ldc "a941"
pop
ldc "a942"
pop
ldc "a943"
pop
ldc "a944"
pop
ldc "a945"
pop
ldc "a946"
pop
ldc "a947"
pop
ldc "a948"
pop
ldc "a949"
pop
ldc "a950"
pop
ldc "a951"
pop
ldc "a952"
pop
ldc "a953"
pop
ldc "a954"
pop
ldc "a955"
pop
ldc "a956"
pop
ldc "a957"
pop
ldc "a958"
pop
ldc "a959"
pop
ldc "a960"
pop
ldc "a961"
pop
ldc "a962"
pop
ldc "a963"
pop
ldc "a964"
pop
ldc "a965"
pop
ldc "a966"
pop
ldc "a967"
pop
ldc "a968"
pop
ldc "a969"
pop
ldc "a970"
pop
ldc "a971"
pop
ldc "a972"
pop
ldc "a973"
pop
ldc "a974"
pop
ldc "a975"
pop
ldc "a976"
pop
ldc "a977"
pop
ldc "a978"
pop
ldc "a979"
pop
ldc "a980"
pop
ldc "a981"
pop
ldc "a982"
pop
ldc "a983"
pop
ldc "a984"
pop
ldc "a985"
pop
ldc "a986"
pop
ldc "a987"
pop
ldc "a988"
pop
ldc "a989"
pop
ldc "a990"
pop
ldc "a991"
pop
ldc "a992"
pop
ldc "a993"
pop
ldc "a994"
pop
ldc "a995"
pop
ldc "a996"
pop
ldc "a997"
pop
ldc "a998"
pop
ldc "a999"
pop
;----------------------------------------

   ldc_w "Hello!" ; must push Hello!
   ldc_w "Hello!" ; must push Hello!
   if_acmpne Fail ; if value != value
   sipush 104
   ireturn
Fail:
   sipush 105
   ireturn

.end method

;
; standard main function
.method public static main([Ljava/lang/String;)V
  .limit stack 2
  .limit locals 1

  new org/apache/harmony/vts/test/vm/jvms/instructions/loadStore/ldc_w/ldc_w03/ldc_w0301/ldc_w0301p
  dup
  invokespecial org/apache/harmony/vts/test/vm/jvms/instructions/loadStore/ldc_w/ldc_w03/ldc_w0301/ldc_w0301p/<init>()V
  aload_0
  invokevirtual org/apache/harmony/vts/test/vm/jvms/instructions/loadStore/ldc_w/ldc_w03/ldc_w0301/ldc_w0301p/test([Ljava/lang/String;)I
  invokestatic java/lang/System/exit(I)V

  return
.end method
