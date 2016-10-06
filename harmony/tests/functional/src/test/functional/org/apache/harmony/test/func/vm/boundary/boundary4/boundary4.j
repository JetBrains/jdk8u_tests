;  Licensed to the Apache Software Foundation (ASF) under one or more
;  contributor license agreements.  See the NOTICE file distributed with
;  this work for additional information regarding copyright ownership.
;  The ASF licenses this file to You under the Apache License, Version 2.0
;  (the "License"); you may not use this file except in compliance with
;  the License.  You may obtain a copy of the License at
;
;     http://www.apache.org/licenses/LICENSE-2.0
;
;  Unless required by applicable law or agreed to in writing, software
;  distributed under the License is distributed on an "AS IS" BASIS,
;  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
;  See the License for the specific language governing permissions and
;  limitations under the License.
;
;
;
.class public org/apache/harmony/test/func/vm/boundary/boundary4/boundary4
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

   .catch all from L1 to L2 using L2
L1:
   aload_0
   ;invoke method
   invokevirtual org/apache/harmony/test/func/vm/boundary/boundary4/boundary4/pTest()I
   ireturn 
L2:   
   sipush 105
   ireturn

.end method

.method pTest()I
   .limit stack 20
   .limit locals 65535
iconst_0
istore 1
iconst_0
istore 2
iconst_0
istore 3
iconst_0
istore 0
iconst_0
istore 100
iconst_0
istore 200
iconst_0
istore 300
iconst_0
istore 400
iconst_0
istore 500
iconst_0
istore 600
iconst_0
istore 700
iconst_0
istore 800
iconst_0
istore 900
iconst_0
istore 1000
iconst_0
istore 1100
iconst_0
istore 1200
iconst_0
istore 1300
iconst_0
istore 1400
iconst_0
istore 1500
iconst_0
istore 1600
iconst_0
istore 1700
iconst_0
istore 1800
iconst_0
istore 1900
iconst_0
istore 2000
iconst_0
istore 2100
iconst_0
istore 2200
iconst_0
istore 2300
iconst_0
istore 2400
iconst_0
istore 2500
iconst_0
istore 2600
iconst_0
istore 2700
iconst_0
istore 2800
iconst_0
istore 2900
iconst_0
istore 3000
iconst_0
istore 3100
iconst_0
istore 3200
iconst_0
istore 3300
iconst_0
istore 3400
iconst_0
istore 3500
iconst_0
istore 3600
iconst_0
istore 3700
iconst_0
istore 3800
iconst_0
istore 3900
iconst_0
istore 4000
iconst_0
istore 4100
iconst_0
istore 4200
iconst_0
istore 4300
iconst_0
istore 4400
iconst_0
istore 4500
iconst_0
istore 4600
iconst_0
istore 4700
iconst_0
istore 4800
iconst_0
istore 4900
iconst_0
istore 5000
iconst_0
istore 5100
iconst_0
istore 5200
iconst_0
istore 5300
iconst_0
istore 5400
iconst_0
istore 5500
iconst_0
istore 5600
iconst_0
istore 5700
iconst_0
istore 5800
iconst_0
istore 5900
iconst_0
istore 6000
iconst_0
istore 6100
iconst_0
istore 6200
iconst_0
istore 6300
iconst_0
istore 6400
iconst_0
istore 6500
iconst_0
istore 6600
iconst_0
istore 6700
iconst_0
istore 6800
iconst_0
istore 6900
iconst_0
istore 7000
iconst_0
istore 7100
iconst_0
istore 7200
iconst_0
istore 7300
iconst_0
istore 7400
iconst_0
istore 7500
iconst_0
istore 7600
iconst_0
istore 7700
iconst_0
istore 7800
iconst_0
istore 7900
iconst_0
istore 8000
iconst_0
istore 8100
iconst_0
istore 8200
iconst_0
istore 8300
iconst_0
istore 8400
iconst_0
istore 8500
iconst_0
istore 8600
iconst_0
istore 8700
iconst_0
istore 8800
iconst_0
istore 8900
iconst_0
istore 9000
iconst_0
istore 9100
iconst_0
istore 9200
iconst_0
istore 9300
iconst_0
istore 9400
iconst_0
istore 9500
iconst_0
istore 9600
iconst_0
istore 9700
iconst_0
istore 9800
iconst_0
istore 9900
iconst_0
istore 10000
iconst_0
istore 10100
iconst_0
istore 10200
iconst_0
istore 10300
iconst_0
istore 10400
iconst_0
istore 10500
iconst_0
istore 10600
iconst_0
istore 10700
iconst_0
istore 10800
iconst_0
istore 10900
iconst_0
istore 11000
iconst_0
istore 11100
iconst_0
istore 11200
iconst_0
istore 11300
iconst_0
istore 11400
iconst_0
istore 11500
iconst_0
istore 11600
iconst_0
istore 11700
iconst_0
istore 11800
iconst_0
istore 11900
iconst_0
istore 12000
iconst_0
istore 12100
iconst_0
istore 12200
iconst_0
istore 12300
iconst_0
istore 12400
iconst_0
istore 12500
iconst_0
istore 12600
iconst_0
istore 12700
iconst_0
istore 12800
iconst_0
istore 12900
iconst_0
istore 13000
iconst_0
istore 13100
iconst_0
istore 13200
iconst_0
istore 13300
iconst_0
istore 13400
iconst_0
istore 13500
iconst_0
istore 13600
iconst_0
istore 13700
iconst_0
istore 13800
iconst_0
istore 13900
iconst_0
istore 14000
iconst_0
istore 14100
iconst_0
istore 14200
iconst_0
istore 14300
iconst_0
istore 14400
iconst_0
istore 14500
iconst_0
istore 14600
iconst_0
istore 14700
iconst_0
istore 14800
iconst_0
istore 14900
iconst_0
istore 15000
iconst_0
istore 15100
iconst_0
istore 15200
iconst_0
istore 15300
iconst_0
istore 15400
iconst_0
istore 15500
iconst_0
istore 15600
iconst_0
istore 15700
iconst_0
istore 15800
iconst_0
istore 15900
iconst_0
istore 16000
iconst_0
istore 16100
iconst_0
istore 16200
iconst_0
istore 16300
iconst_0
istore 16400
iconst_0
istore 16500
iconst_0
istore 16600
iconst_0
istore 16700
iconst_0
istore 16800
iconst_0
istore 16900
iconst_0
istore 17000
iconst_0
istore 17100
iconst_0
istore 17200
iconst_0
istore 17300
iconst_0
istore 17400
iconst_0
istore 17500
iconst_0
istore 17600
iconst_0
istore 17700
iconst_0
istore 17800
iconst_0
istore 17900
iconst_0
istore 18000
iconst_0
istore 18100
iconst_0
istore 18200
iconst_0
istore 18300
iconst_0
istore 18400
iconst_0
istore 18500
iconst_0
istore 18600
iconst_0
istore 18700
iconst_0
istore 18800
iconst_0
istore 18900
iconst_0
istore 19000
iconst_0
istore 19100
iconst_0
istore 19200
iconst_0
istore 19300
iconst_0
istore 19400
iconst_0
istore 19500
iconst_0
istore 19600
iconst_0
istore 19700
iconst_0
istore 19800
iconst_0
istore 19900
iconst_0
istore 20000
iconst_0
istore 20100
iconst_0
istore 20200
iconst_0
istore 20300
iconst_0
istore 20400
iconst_0
istore 20500
iconst_0
istore 20600
iconst_0
istore 20700
iconst_0
istore 20800
iconst_0
istore 20900
iconst_0
istore 21000
iconst_0
istore 21100
iconst_0
istore 21200
iconst_0
istore 21300
iconst_0
istore 21400
iconst_0
istore 21500
iconst_0
istore 21600
iconst_0
istore 21700
iconst_0
istore 21800
iconst_0
istore 21900
iconst_0
istore 22000
iconst_0
istore 22100
iconst_0
istore 22200
iconst_0
istore 22300
iconst_0
istore 22400
iconst_0
istore 22500
iconst_0
istore 22600
iconst_0
istore 22700
iconst_0
istore 22800
iconst_0
istore 22900
iconst_0
istore 23000
iconst_0
istore 23100
iconst_0
istore 23200
iconst_0
istore 23300
iconst_0
istore 23400
iconst_0
istore 23500
iconst_0
istore 23600
iconst_0
istore 23700
iconst_0
istore 23800
iconst_0
istore 23900
iconst_0
istore 24000
iconst_0
istore 24100
iconst_0
istore 24200
iconst_0
istore 24300
iconst_0
istore 24400
iconst_0
istore 24500
iconst_0
istore 24600
iconst_0
istore 24700
iconst_0
istore 24800
iconst_0
istore 24900
iconst_0
istore 25000
iconst_0
istore 25100
iconst_0
istore 25200
iconst_0
istore 25300
iconst_0
istore 25400
iconst_0
istore 25500
iconst_0
istore 25600
iconst_0
istore 25700
iconst_0
istore 25800
iconst_0
istore 25900
iconst_0
istore 26000
iconst_0
istore 26100
iconst_0
istore 26200
iconst_0
istore 26300
iconst_0
istore 26400
iconst_0
istore 26500
iconst_0
istore 26600
iconst_0
istore 26700
iconst_0
istore 26800
iconst_0
istore 26900
iconst_0
istore 27000
iconst_0
istore 27100
iconst_0
istore 27200
iconst_0
istore 27300
iconst_0
istore 27400
iconst_0
istore 27500
iconst_0
istore 27600
iconst_0
istore 27700
iconst_0
istore 27800
iconst_0
istore 27900
iconst_0
istore 28000
iconst_0
istore 28100
iconst_0
istore 28200
iconst_0
istore 28300
iconst_0
istore 28400
iconst_0
istore 28500
iconst_0
istore 28600
iconst_0
istore 28700
iconst_0
istore 28800
iconst_0
istore 28900
iconst_0
istore 29000
iconst_0
istore 29100
iconst_0
istore 29200
iconst_0
istore 29300
iconst_0
istore 29400
iconst_0
istore 29500
iconst_0
istore 29600
iconst_0
istore 29700
iconst_0
istore 29800
iconst_0
istore 29900
iconst_0
istore 30000
iconst_0
istore 30100
iconst_0
istore 30200
iconst_0
istore 30300
iconst_0
istore 30400
iconst_0
istore 30500
iconst_0
istore 30600
iconst_0
istore 30700
iconst_0
istore 30800
iconst_0
istore 30900
iconst_0
istore 31000
iconst_0
istore 31100
iconst_0
istore 31200
iconst_0
istore 31300
iconst_0
istore 31400
iconst_0
istore 31500
iconst_0
istore 31600
iconst_0
istore 31700
iconst_0
istore 31800
iconst_0
istore 31900
iconst_0
istore 32000
iconst_0
istore 32100
iconst_0
istore 32200
iconst_0
istore 32300
iconst_0
istore 32400
iconst_0
istore 32500
iconst_0
istore 32600
iconst_0
istore 32700
iconst_0
istore 32800
iconst_0
istore 32900
iconst_0
istore 33000
iconst_0
istore 33100
iconst_0
istore 33200
iconst_0
istore 33300
iconst_0
istore 33400
iconst_0
istore 33500
iconst_0
istore 33600
iconst_0
istore 33700
iconst_0
istore 33800
iconst_0
istore 33900
iconst_0
istore 34000
iconst_0
istore 34100
iconst_0
istore 34200
iconst_0
istore 34300
iconst_0
istore 34400
iconst_0
istore 34500
iconst_0
istore 34600
iconst_0
istore 34700
iconst_0
istore 34800
iconst_0
istore 34900
iconst_0
istore 35000
iconst_0
istore 35100
iconst_0
istore 35200
iconst_0
istore 35300
iconst_0
istore 35400
iconst_0
istore 35500
iconst_0
istore 35600
iconst_0
istore 35700
iconst_0
istore 35800
iconst_0
istore 35900
iconst_0
istore 36000
iconst_0
istore 36100
iconst_0
istore 36200
iconst_0
istore 36300
iconst_0
istore 36400
iconst_0
istore 36500
iconst_0
istore 36600
iconst_0
istore 36700
iconst_0
istore 36800
iconst_0
istore 36900
iconst_0
istore 37000
iconst_0
istore 37100
iconst_0
istore 37200
iconst_0
istore 37300
iconst_0
istore 37400
iconst_0
istore 37500
iconst_0
istore 37600
iconst_0
istore 37700
iconst_0
istore 37800
iconst_0
istore 37900
iconst_0
istore 38000
iconst_0
istore 38100
iconst_0
istore 38200
iconst_0
istore 38300
iconst_0
istore 38400
iconst_0
istore 38500
iconst_0
istore 38600
iconst_0
istore 38700
iconst_0
istore 38800
iconst_0
istore 38900
iconst_0
istore 39000
iconst_0
istore 39100
iconst_0
istore 39200
iconst_0
istore 39300
iconst_0
istore 39400
iconst_0
istore 39500
iconst_0
istore 39600
iconst_0
istore 39700
iconst_0
istore 39800
iconst_0
istore 39900
iconst_0
istore 40000
iconst_0
istore 40100
iconst_0
istore 40200
iconst_0
istore 40300
iconst_0
istore 40400
iconst_0
istore 40500
iconst_0
istore 40600
iconst_0
istore 40700
iconst_0
istore 40800
iconst_0
istore 40900
iconst_0
istore 41000
iconst_0
istore 41100
iconst_0
istore 41200
iconst_0
istore 41300
iconst_0
istore 41400
iconst_0
istore 41500
iconst_0
istore 41600
iconst_0
istore 41700
iconst_0
istore 41800
iconst_0
istore 41900
iconst_0
istore 42000
iconst_0
istore 42100
iconst_0
istore 42200
iconst_0
istore 42300
iconst_0
istore 42400
iconst_0
istore 42500
iconst_0
istore 42600
iconst_0
istore 42700
iconst_0
istore 42800
iconst_0
istore 42900
iconst_0
istore 43000
iconst_0
istore 43100
iconst_0
istore 43200
iconst_0
istore 43300
iconst_0
istore 43400
iconst_0
istore 43500
iconst_0
istore 43600
iconst_0
istore 43700
iconst_0
istore 43800
iconst_0
istore 43900
iconst_0
istore 44000
iconst_0
istore 44100
iconst_0
istore 44200
iconst_0
istore 44300
iconst_0
istore 44400
iconst_0
istore 44500
iconst_0
istore 44600
iconst_0
istore 44700
iconst_0
istore 44800
iconst_0
istore 44900
iconst_0
istore 45000
iconst_0
istore 45100
iconst_0
istore 45200
iconst_0
istore 45300
iconst_0
istore 45400
iconst_0
istore 45500
iconst_0
istore 45600
iconst_0
istore 45700
iconst_0
istore 45800
iconst_0
istore 45900
iconst_0
istore 46000
iconst_0
istore 46100
iconst_0
istore 46200
iconst_0
istore 46300
iconst_0
istore 46400
iconst_0
istore 46500
iconst_0
istore 46600
iconst_0
istore 46700
iconst_0
istore 46800
iconst_0
istore 46900
iconst_0
istore 47000
iconst_0
istore 47100
iconst_0
istore 47200
iconst_0
istore 47300
iconst_0
istore 47400
iconst_0
istore 47500
iconst_0
istore 47600
iconst_0
istore 47700
iconst_0
istore 47800
iconst_0
istore 47900
iconst_0
istore 48000
iconst_0
istore 48100
iconst_0
istore 48200
iconst_0
istore 48300
iconst_0
istore 48400
iconst_0
istore 48500
iconst_0
istore 48600
iconst_0
istore 48700
iconst_0
istore 48800
iconst_0
istore 48900
iconst_0
istore 49000
iconst_0
istore 49100
iconst_0
istore 49200
iconst_0
istore 49300
iconst_0
istore 49400
iconst_0
istore 49500
iconst_0
istore 49600
iconst_0
istore 49700
iconst_0
istore 49800
iconst_0
istore 49900
iconst_0
istore 50000
iconst_0
istore 50100
iconst_0
istore 50200
iconst_0
istore 50300
iconst_0
istore 50400
iconst_0
istore 50500
iconst_0
istore 50600
iconst_0
istore 50700
iconst_0
istore 50800
iconst_0
istore 50900
iconst_0
istore 51000
iconst_0
istore 51100
iconst_0
istore 51200
iconst_0
istore 51300
iconst_0
istore 51400
iconst_0
istore 51500
iconst_0
istore 51600
iconst_0
istore 51700
iconst_0
istore 51800
iconst_0
istore 51900
iconst_0
istore 52000
iconst_0
istore 52100
iconst_0
istore 52200
iconst_0
istore 52300
iconst_0
istore 52400
iconst_0
istore 52500
iconst_0
istore 52600
iconst_0
istore 52700
iconst_0
istore 52800
iconst_0
istore 52900
iconst_0
istore 53000
iconst_0
istore 53100
iconst_0
istore 53200
iconst_0
istore 53300
iconst_0
istore 53400
iconst_0
istore 53500
iconst_0
istore 53600
iconst_0
istore 53700
iconst_0
istore 53800
iconst_0
istore 53900
iconst_0
istore 54000
iconst_0
istore 54100
iconst_0
istore 54200
iconst_0
istore 54300
iconst_0
istore 54400
iconst_0
istore 54500
iconst_0
istore 54600
iconst_0
istore 54700
iconst_0
istore 54800
iconst_0
istore 54900
iconst_0
istore 55000
iconst_0
istore 55100
iconst_0
istore 55200
iconst_0
istore 55300
iconst_0
istore 55400
iconst_0
istore 55500
iconst_0
istore 55600
iconst_0
istore 55700
iconst_0
istore 55800
iconst_0
istore 55900
iconst_0
istore 56000
iconst_0
istore 56100
iconst_0
istore 56200
iconst_0
istore 56300
iconst_0
istore 56400
iconst_0
istore 56500
iconst_0
istore 56600
iconst_0
istore 56700
iconst_0
istore 56800
iconst_0
istore 56900
iconst_0
istore 57000
iconst_0
istore 57100
iconst_0
istore 57200
iconst_0
istore 57300
iconst_0
istore 57400
iconst_0
istore 57500
iconst_0
istore 57600
iconst_0
istore 57700
iconst_0
istore 57800
iconst_0
istore 57900
iconst_0
istore 58000
iconst_0
istore 58100
iconst_0
istore 58200
iconst_0
istore 58300
iconst_0
istore 58400
iconst_0
istore 58500
iconst_0
istore 58600
iconst_0
istore 58700
iconst_0
istore 58800
iconst_0
istore 58900
iconst_0
istore 59000
iconst_0
istore 59100
iconst_0
istore 59200
iconst_0
istore 59300
iconst_0
istore 59400
iconst_0
istore 59500
iconst_0
istore 59600
iconst_0
istore 59700
iconst_0
istore 59800
iconst_0
istore 59900
iconst_0
istore 60000
iconst_0
istore 60100
iconst_0
istore 60200
iconst_0
istore 60300
iconst_0
istore 60400
iconst_0
istore 60500
iconst_0
istore 60600
iconst_0
istore 60700
iconst_0
istore 60800
iconst_0
istore 60900
iconst_0
istore 61000
iconst_0
istore 61100
iconst_0
istore 61200
iconst_0
istore 61300
iconst_0
istore 61400
iconst_0
istore 61500
iconst_0
istore 61600
iconst_0
istore 61700
iconst_0
istore 61800
iconst_0
istore 61900
iconst_0
istore 62000
iconst_0
istore 62100
iconst_0
istore 62200
iconst_0
istore 62300
iconst_0
istore 62400
iconst_0
istore 62500
iconst_0
istore 62600
iconst_0
istore 62700
iconst_0
istore 62800
iconst_0
istore 62900
iconst_0
istore 63000
iconst_0
istore 63100
iconst_0
istore 63200
iconst_0
istore 63300
iconst_0
istore 63400
iconst_0
istore 63500
iconst_0
istore 63600
iconst_0
istore 63700
iconst_0
istore 63800
iconst_0
istore 63900
iconst_0
istore 64000
iconst_0
istore 64100
iconst_0
istore 64200
iconst_0
istore 64300
iconst_0
istore 64400
iconst_0
istore 64500
iconst_0
istore 64600
iconst_0
istore 64700
iconst_0
istore 64800
iconst_0
istore 64900
iconst_0
istore 65000
iconst_0
istore 65100
iconst_0
istore 65200
iconst_0
istore 65300
iconst_0
istore 65400
iconst_0
istore 65500


iconst_0
istore 65501
iconst_0
istore 65502
iconst_0
istore 65503
iconst_0
istore 65504
iconst_0
istore 65505
iconst_0
istore 65506
iconst_0
istore 65507
iconst_0
istore 65508
iconst_0
istore 65509
iconst_0
istore 65510
iconst_0
istore 65511
iconst_0
istore 65512
iconst_0
istore 65513
iconst_0
istore 65514
iconst_0
istore 65515
iconst_0
istore 65516
iconst_0
istore 65517
iconst_0
istore 65518
iconst_0
istore 65519
iconst_0
istore 65520
iconst_0
istore 65521
iconst_0
istore 65522
iconst_0
istore 65523
iconst_0
istore 65524
iconst_0
istore 65525
iconst_0
istore 65526
iconst_0
istore 65527
iconst_0
istore 65528
iconst_0
istore 65529
iconst_0
istore 65530
iconst_0
istore 65531
iconst_0
istore 65532
iconst_0
istore 65533

iconst_0
istore 65534


   sipush 104
   ireturn
.end method



;
; standard main functon
.method public static main([Ljava/lang/String;)V
  .limit stack 2
  .limit locals 1

  new org/apache/harmony/test/func/vm/boundary/boundary4/boundary4
  dup
  invokespecial org/apache/harmony/test/func/vm/boundary/boundary4/boundary4/<init>()V
  aload_0
  invokevirtual org/apache/harmony/test/func/vm/boundary/boundary4/boundary4/test([Ljava/lang/String;)I
  invokestatic java/lang/System/exit(I)V

  return
.end method
