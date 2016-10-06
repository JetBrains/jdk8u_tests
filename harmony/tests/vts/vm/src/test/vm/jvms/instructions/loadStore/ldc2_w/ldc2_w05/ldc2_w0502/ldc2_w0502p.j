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
; Author: Ilia A. Leviev
; Version: $Revision: 1.3 $
;
.class public org/apache/harmony/vts/test/vm/jvms/instructions/loadStore/ldc2_w/ldc2_w05/ldc2_w0502/ldc2_w0502p
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
   .limit stack 4
   .limit locals 2

   ;if any exception is thrown during execution, then test fail
;;;;;;;;;;;;;;;;;;;;;;
; create constant pool with constant_pool_count  2632
;;;;;;;;;;;;;;;;;;;;;;;

ldc 1
pop
ldc 2
pop
ldc 3
pop
ldc 4
pop
ldc 5
pop
ldc 6
pop
ldc 7
pop
ldc 8
pop
ldc 9
pop
ldc 10
pop
ldc 11
pop
ldc 12
pop
ldc 13
pop
ldc 14
pop
ldc 15
pop
ldc 16
pop
ldc 17
pop
ldc 18
pop
ldc 19
pop
ldc 20
pop
ldc 21
pop
ldc 22
pop
ldc 23
pop
ldc 24
pop
ldc 25
pop
ldc 26
pop
ldc 27
pop
ldc 28
pop
ldc 29
pop
ldc 30
pop
ldc 31
pop
ldc 32
pop
ldc 33
pop
ldc 34
pop
ldc 35
pop
ldc 36
pop
ldc 37
pop
ldc 38
pop
ldc 39
pop
ldc 40
pop
ldc 41
pop
ldc 42
pop
ldc 43
pop
ldc 44
pop
ldc 45
pop
ldc 46
pop
ldc 47
pop
ldc 48
pop
ldc 49
pop
ldc 50
pop
ldc 51
pop
ldc 52
pop
ldc 53
pop
ldc 54
pop
ldc 55
pop
ldc 56
pop
ldc 57
pop
ldc 58
pop
ldc 59
pop
ldc 60
pop
ldc 61
pop
ldc 62
pop
ldc 63
pop
ldc 64
pop
ldc 65
pop
ldc 66
pop
ldc 67
pop
ldc 68
pop
ldc 69
pop
ldc 70
pop
ldc 71
pop
ldc 72
pop
ldc 73
pop
ldc 74
pop
ldc 75
pop
ldc 76
pop
ldc 77
pop
ldc 78
pop
ldc 79
pop
ldc 80
pop
ldc 81
pop
ldc 82
pop
ldc 83
pop
ldc 84
pop
ldc 85
pop
ldc 86
pop
ldc 87
pop
ldc 88
pop
ldc 89
pop
ldc 90
pop
ldc 91
pop
ldc 92
pop
ldc 93
pop
ldc 94
pop
ldc 95
pop
ldc 96
pop
ldc 97
pop
ldc 98
pop
ldc 99
pop
ldc 100
pop
ldc 101
pop
ldc 102
pop
ldc 103
pop
ldc 104
pop
ldc 105
pop
ldc 106
pop
ldc 107
pop
ldc 108
pop
ldc 109
pop
ldc 110
pop
ldc 111
pop
ldc 112
pop
ldc 113
pop
ldc 114
pop
ldc 115
pop
ldc 116
pop
ldc 117
pop
ldc 118
pop
ldc 119
pop
ldc 120
pop
ldc 121
pop
ldc 122
pop
ldc 123
pop
ldc 124
pop
ldc 125
pop
ldc 126
pop
ldc 127
pop
ldc 128
pop
ldc 129
pop
ldc 130
pop
ldc 131
pop
ldc 132
pop
ldc 133
pop
ldc 134
pop
ldc 135
pop
ldc 136
pop
ldc 137
pop
ldc 138
pop
ldc 139
pop
ldc 140
pop
ldc 141
pop
ldc 142
pop
ldc 143
pop
ldc 144
pop
ldc 145
pop
ldc 146
pop
ldc 147
pop
ldc 148
pop
ldc 149
pop
ldc 150
pop
ldc 151
pop
ldc 152
pop
ldc 153
pop
ldc 154
pop
ldc 155
pop
ldc 156
pop
ldc 157
pop
ldc 158
pop
ldc 159
pop
ldc 160
pop
ldc 161
pop
ldc 162
pop
ldc 163
pop
ldc 164
pop
ldc 165
pop
ldc 166
pop
ldc 167
pop
ldc 168
pop
ldc 169
pop
ldc 170
pop
ldc 171
pop
ldc 172
pop
ldc 173
pop
ldc 174
pop
ldc 175
pop
ldc 176
pop
ldc 177
pop
ldc 178
pop
ldc 179
pop
ldc 180
pop
ldc 181
pop
ldc 182
pop
ldc 183
pop
ldc 184
pop
ldc 185
pop
ldc 186
pop
ldc 187
pop
ldc 188
pop
ldc 189
pop
ldc 190
pop
ldc 191
pop
ldc 192
pop
ldc 193
pop
ldc 194
pop
ldc 195
pop
ldc 196
pop
ldc 197
pop
ldc 198
pop
ldc 199
pop
ldc 200
pop
ldc 201
pop
ldc 202
pop
ldc 203
pop
ldc 204
pop
ldc 205
pop
ldc 206
pop
ldc 207
pop
ldc 208
pop
ldc 209
pop
ldc 210
pop
ldc 211
pop
ldc 212
pop
ldc 213
pop
ldc 214
pop
ldc 215
pop
ldc 216
pop
ldc 217
pop
ldc 218
pop
ldc 219
pop
ldc 220
pop
ldc 221
pop
ldc 222
pop
ldc 223
pop
ldc 224
pop
ldc 225
pop
ldc 226
pop
ldc 227
pop
ldc 228
pop
ldc 229
pop
ldc 230
pop
ldc 231
pop
ldc 232
pop
ldc 233
pop
ldc 234
pop
ldc 235
pop
ldc 236
pop
ldc 237
pop
ldc 238
pop
ldc 239
pop
ldc 240
pop
ldc 241
pop
ldc 242
pop
ldc 243
pop
ldc 244
pop
ldc 245
pop
ldc 246
pop
ldc 247
pop
ldc 248
pop
ldc 249
pop
ldc 250
pop
ldc 251
pop
ldc 252
pop
ldc 253
pop
ldc 254
pop
ldc 255
pop
ldc 256
pop
ldc 257
pop
ldc 258
pop
ldc 259
pop
ldc 260
pop
ldc 261
pop
ldc 262
pop
ldc 263
pop
ldc 264
pop
ldc 265
pop
ldc 266
pop
ldc 267
pop
ldc 268
pop
ldc 269
pop
ldc 270
pop
ldc 271
pop
ldc 272
pop
ldc 273
pop
ldc 274
pop
ldc 275
pop
ldc 276
pop
ldc 277
pop
ldc 278
pop
ldc 279
pop
ldc 280
pop
ldc 281
pop
ldc 282
pop
ldc 283
pop
ldc 284
pop
ldc 285
pop
ldc 286
pop
ldc 287
pop
ldc 288
pop
ldc 289
pop
ldc 290
pop
ldc 291
pop
ldc 292
pop
ldc 293
pop
ldc 294
pop
ldc 295
pop
ldc 296
pop
ldc 297
pop
ldc 298
pop
ldc 299
pop
ldc 300
pop
ldc 301
pop
ldc 302
pop
ldc 303
pop
ldc 304
pop
ldc 305
pop
ldc 306
pop
ldc 307
pop
ldc 308
pop
ldc 309
pop
ldc 310
pop
ldc 311
pop
ldc 312
pop
ldc 313
pop
ldc 314
pop
ldc 315
pop
ldc 316
pop
ldc 317
pop
ldc 318
pop
ldc 319
pop
ldc 320
pop
ldc 321
pop
ldc 322
pop
ldc 323
pop
ldc 324
pop
ldc 325
pop
ldc 326
pop
ldc 327
pop
ldc 328
pop
ldc 329
pop
ldc 330
pop
ldc 331
pop
ldc 332
pop
ldc 333
pop
ldc 334
pop
ldc 335
pop
ldc 336
pop
ldc 337
pop
ldc 338
pop
ldc 339
pop
ldc 340
pop
ldc 341
pop
ldc 342
pop
ldc 343
pop
ldc 344
pop
ldc 345
pop
ldc 346
pop
ldc 347
pop
ldc 348
pop
ldc 349
pop
ldc 350
pop
ldc 351
pop
ldc 352
pop
ldc 353
pop
ldc 354
pop
ldc 355
pop
ldc 356
pop
ldc 357
pop
ldc 358
pop
ldc 359
pop
ldc 360
pop
ldc 361
pop
ldc 362
pop
ldc 363
pop
ldc 364
pop
ldc 365
pop
ldc 366
pop
ldc 367
pop
ldc 368
pop
ldc 369
pop
ldc 370
pop
ldc 371
pop
ldc 372
pop
ldc 373
pop
ldc 374
pop
ldc 375
pop
ldc 376
pop
ldc 377
pop
ldc 378
pop
ldc 379
pop
ldc 380
pop
ldc 381
pop
ldc 382
pop
ldc 383
pop
ldc 384
pop
ldc 385
pop
ldc 386
pop
ldc 387
pop
ldc 388
pop
ldc 389
pop
ldc 390
pop
ldc 391
pop
ldc 392
pop
ldc 393
pop
ldc 394
pop
ldc 395
pop
ldc 396
pop
ldc 397
pop
ldc 398
pop
ldc 399
pop
ldc 400
pop
ldc 401
pop
ldc 402
pop
ldc 403
pop
ldc 404
pop
ldc 405
pop
ldc 406
pop
ldc 407
pop
ldc 408
pop
ldc 409
pop
ldc 410
pop
ldc 411
pop
ldc 412
pop
ldc 413
pop
ldc 414
pop
ldc 415
pop
ldc 416
pop
ldc 417
pop
ldc 418
pop
ldc 419
pop
ldc 420
pop
ldc 421
pop
ldc 422
pop
ldc 423
pop
ldc 424
pop
ldc 425
pop
ldc 426
pop
ldc 427
pop
ldc 428
pop
ldc 429
pop
ldc 430
pop
ldc 431
pop
ldc 432
pop
ldc 433
pop
ldc 434
pop
ldc 435
pop
ldc 436
pop
ldc 437
pop
ldc 438
pop
ldc 439
pop
ldc 440
pop
ldc 441
pop
ldc 442
pop
ldc 443
pop
ldc 444
pop
ldc 445
pop
ldc 446
pop
ldc 447
pop
ldc 448
pop
ldc 449
pop
ldc 450
pop
ldc 451
pop
ldc 452
pop
ldc 453
pop
ldc 454
pop
ldc 455
pop
ldc 456
pop
ldc 457
pop
ldc 458
pop
ldc 459
pop
ldc 460
pop
ldc 461
pop
ldc 462
pop
ldc 463
pop
ldc 464
pop
ldc 465
pop
ldc 466
pop
ldc 467
pop
ldc 468
pop
ldc 469
pop
ldc 470
pop
ldc 471
pop
ldc 472
pop
ldc 473
pop
ldc 474
pop
ldc 475
pop
ldc 476
pop
ldc 477
pop
ldc 478
pop
ldc 479
pop
ldc 480
pop
ldc 481
pop
ldc 482
pop
ldc 483
pop
ldc 484
pop
ldc 485
pop
ldc 486
pop
ldc 487
pop
ldc 488
pop
ldc 489
pop
ldc 490
pop
ldc 491
pop
ldc 492
pop
ldc 493
pop
ldc 494
pop
ldc 495
pop
ldc 496
pop
ldc 497
pop
ldc 498
pop
ldc 499
pop
ldc 500
pop
ldc 501
pop
ldc 502
pop
ldc 503
pop
ldc 504
pop
ldc 505
pop
ldc 506
pop
ldc 507
pop
ldc 508
pop
ldc 509
pop
ldc 510
pop
ldc 511
pop
ldc 512
pop
ldc 513
pop
ldc 514
pop
ldc 515
pop
ldc 516
pop
ldc 517
pop
ldc 518
pop
ldc 519
pop
ldc 520
pop
ldc 521
pop
ldc 522
pop
ldc 523
pop
ldc 524
pop
ldc 525
pop
ldc 526
pop
ldc 527
pop
ldc 528
pop
ldc 529
pop
ldc 530
pop
ldc 531
pop
ldc 532
pop
ldc 533
pop
ldc 534
pop
ldc 535
pop
ldc 536
pop
ldc 537
pop
ldc 538
pop
ldc 539
pop
ldc 540
pop
ldc 541
pop
ldc 542
pop
ldc 543
pop
ldc 544
pop
ldc 545
pop
ldc 546
pop
ldc 547
pop
ldc 548
pop
ldc 549
pop
ldc 550
pop
ldc 551
pop
ldc 552
pop
ldc 553
pop
ldc 554
pop
ldc 555
pop
ldc 556
pop
ldc 557
pop
ldc 558
pop
ldc 559
pop
ldc 560
pop
ldc 561
pop
ldc 562
pop
ldc 563
pop
ldc 564
pop
ldc 565
pop
ldc 566
pop
ldc 567
pop
ldc 568
pop
ldc 569
pop
ldc 570
pop
ldc 571
pop
ldc 572
pop
ldc 573
pop
ldc 574
pop
ldc 575
pop
ldc 576
pop
ldc 577
pop
ldc 578
pop
ldc 579
pop
ldc 580
pop
ldc 581
pop
ldc 582
pop
ldc 583
pop
ldc 584
pop
ldc 585
pop
ldc 586
pop
ldc 587
pop
ldc 588
pop
ldc 589
pop
ldc 590
pop
ldc 591
pop
ldc 592
pop
ldc 593
pop
ldc 594
pop
ldc 595
pop
ldc 596
pop
ldc 597
pop
ldc 598
pop
ldc 599
pop
ldc 600
pop
ldc 601
pop
ldc 602
pop
ldc 603
pop
ldc 604
pop
ldc 605
pop
ldc 606
pop
ldc 607
pop
ldc 608
pop
ldc 609
pop
ldc 610
pop
ldc 611
pop
ldc 612
pop
ldc 613
pop
ldc 614
pop
ldc 615
pop
ldc 616
pop
ldc 617
pop
ldc 618
pop
ldc 619
pop
ldc 620
pop
ldc 621
pop
ldc 622
pop
ldc 623
pop
ldc 624
pop
ldc 625
pop
ldc 626
pop
ldc 627
pop
ldc 628
pop
ldc 629
pop
ldc 630
pop
ldc 631
pop
ldc 632
pop
ldc 633
pop
ldc 634
pop
ldc 635
pop
ldc 636
pop
ldc 637
pop
ldc 638
pop
ldc 639
pop
ldc 640
pop
ldc 641
pop
ldc 642
pop
ldc 643
pop
ldc 644
pop
ldc 645
pop
ldc 646
pop
ldc 647
pop
ldc 648
pop
ldc 649
pop
ldc 650
pop
ldc 651
pop
ldc 652
pop
ldc 653
pop
ldc 654
pop
ldc 655
pop
ldc 656
pop
ldc 657
pop
ldc 658
pop
ldc 659
pop
ldc 660
pop
ldc 661
pop
ldc 662
pop
ldc 663
pop
ldc 664
pop
ldc 665
pop
ldc 666
pop
ldc 667
pop
ldc 668
pop
ldc 669
pop
ldc 670
pop
ldc 671
pop
ldc 672
pop
ldc 673
pop
ldc 674
pop
ldc 675
pop
ldc 676
pop
ldc 677
pop
ldc 678
pop
ldc 679
pop
ldc 680
pop
ldc 681
pop
ldc 682
pop
ldc 683
pop
ldc 684
pop
ldc 685
pop
ldc 686
pop
ldc 687
pop
ldc 688
pop
ldc 689
pop
ldc 690
pop
ldc 691
pop
ldc 692
pop
ldc 693
pop
ldc 694
pop
ldc 695
pop
ldc 696
pop
ldc 697
pop
ldc 698
pop
ldc 699
pop
ldc 700
pop
ldc 701
pop
ldc 702
pop
ldc 703
pop
ldc 704
pop
ldc 705
pop
ldc 706
pop
ldc 707
pop
ldc 708
pop
ldc 709
pop
ldc 710
pop
ldc 711
pop
ldc 712
pop
ldc 713
pop
ldc 714
pop
ldc 715
pop
ldc 716
pop
ldc 717
pop
ldc 718
pop
ldc 719
pop
ldc 720
pop
ldc 721
pop
ldc 722
pop
ldc 723
pop
ldc 724
pop
ldc 725
pop
ldc 726
pop
ldc 727
pop
ldc 728
pop
ldc 729
pop
ldc 730
pop
ldc 731
pop
ldc 732
pop
ldc 733
pop
ldc 734
pop
ldc 735
pop
ldc 736
pop
ldc 737
pop
ldc 738
pop
ldc 739
pop
ldc 740
pop
ldc 741
pop
ldc 742
pop
ldc 743
pop
ldc 744
pop
ldc 745
pop
ldc 746
pop
ldc 747
pop
ldc 748
pop
ldc 749
pop
ldc 750
pop
ldc 751
pop
ldc 752
pop
ldc 753
pop
ldc 754
pop
ldc 755
pop
ldc 756
pop
ldc 757
pop
ldc 758
pop
ldc 759
pop
ldc 760
pop
ldc 761
pop
ldc 762
pop
ldc 763
pop
ldc 764
pop
ldc 765
pop
ldc 766
pop
ldc 767
pop
ldc 768
pop
ldc 769
pop
ldc 770
pop
ldc 771
pop
ldc 772
pop
ldc 773
pop
ldc 774
pop
ldc 775
pop
ldc 776
pop
ldc 777
pop
ldc 778
pop
ldc 779
pop
ldc 780
pop
ldc 781
pop
ldc 782
pop
ldc 783
pop
ldc 784
pop
ldc 785
pop
ldc 786
pop
ldc 787
pop
ldc 788
pop
ldc 789
pop
ldc 790
pop
ldc 791
pop
ldc 792
pop
ldc 793
pop
ldc 794
pop
ldc 795
pop
ldc 796
pop
ldc 797
pop
ldc 798
pop
ldc 799
pop
ldc 800
pop
ldc 801
pop
ldc 802
pop
ldc 803
pop
ldc 804
pop
ldc 805
pop
ldc 806
pop
ldc 807
pop
ldc 808
pop
ldc 809
pop
ldc 810
pop
ldc 811
pop
ldc 812
pop
ldc 813
pop
ldc 814
pop
ldc 815
pop
ldc 816
pop
ldc 817
pop
ldc 818
pop
ldc 819
pop
ldc 820
pop
ldc 821
pop
ldc 822
pop
ldc 823
pop
ldc 824
pop
ldc 825
pop
ldc 826
pop
ldc 827
pop
ldc 828
pop
ldc 829
pop
ldc 830
pop
ldc 831
pop
ldc 832
pop
ldc 833
pop
ldc 834
pop
ldc 835
pop
ldc 836
pop
ldc 837
pop
ldc 838
pop
ldc 839
pop
ldc 840
pop
ldc 841
pop
ldc 842
pop
ldc 843
pop
ldc 844
pop
ldc 845
pop
ldc 846
pop
ldc 847
pop
ldc 848
pop
ldc 849
pop
ldc 850
pop
ldc 851
pop
ldc 852
pop
ldc 853
pop
ldc 854
pop
ldc 855
pop
ldc 856
pop
ldc 857
pop
ldc 858
pop
ldc 859
pop
ldc 860
pop
ldc 861
pop
ldc 862
pop
ldc 863
pop
ldc 864
pop
ldc 865
pop
ldc 866
pop
ldc 867
pop
ldc 868
pop
ldc 869
pop
ldc 870
pop
ldc 871
pop
ldc 872
pop
ldc 873
pop
ldc 874
pop
ldc 875
pop
ldc 876
pop
ldc 877
pop
ldc 878
pop
ldc 879
pop
ldc 880
pop
ldc 881
pop
ldc 882
pop
ldc 883
pop
ldc 884
pop
ldc 885
pop
ldc 886
pop
ldc 887
pop
ldc 888
pop
ldc 889
pop
ldc 890
pop
ldc 891
pop
ldc 892
pop
ldc 893
pop
ldc 894
pop
ldc 895
pop
ldc 896
pop
ldc 897
pop
ldc 898
pop
ldc 899
pop
ldc 900
pop
ldc 901
pop
ldc 902
pop
ldc 903
pop
ldc 904
pop
ldc 905
pop
ldc 906
pop
ldc 907
pop
ldc 908
pop
ldc 909
pop
ldc 910
pop
ldc 911
pop
ldc 912
pop
ldc 913
pop
ldc 914
pop
ldc 915
pop
ldc 916
pop
ldc 917
pop
ldc 918
pop
ldc 919
pop
ldc 920
pop
ldc 921
pop
ldc 922
pop
ldc 923
pop
ldc 924
pop
ldc 925
pop
ldc 926
pop
ldc 927
pop
ldc 928
pop
ldc 929
pop
ldc 930
pop
ldc 931
pop
ldc 932
pop
ldc 933
pop
ldc 934
pop
ldc 935
pop
ldc 936
pop
ldc 937
pop
ldc 938
pop
ldc 939
pop
ldc 940
pop
ldc 941
pop
ldc 942
pop
ldc 943
pop
ldc 944
pop
ldc 945
pop
ldc 946
pop
ldc 947
pop
ldc 948
pop
ldc 949
pop
ldc 950
pop
ldc 951
pop
ldc 952
pop
ldc 953
pop
ldc 954
pop
ldc 955
pop
ldc 956
pop
ldc 957
pop
ldc 958
pop
ldc 959
pop
ldc 960
pop
ldc 961
pop
ldc 962
pop
ldc 963
pop
ldc 964
pop
ldc 965
pop
ldc 966
pop
ldc 967
pop
ldc 968
pop
ldc 969
pop
ldc 970
pop
ldc 971
pop
ldc 972
pop
ldc 973
pop
ldc 974
pop
ldc 975
pop
ldc 976
pop
ldc 977
pop
ldc 978
pop
ldc 979
pop
ldc 980
pop
ldc 981
pop
ldc 982
pop
ldc 983
pop
ldc 984
pop
ldc 985
pop
ldc 986
pop
ldc 987
pop
ldc 988
pop
ldc 989
pop
ldc 990
pop
ldc 991
pop
ldc 992
pop
ldc 993
pop
ldc 994
pop
ldc 995
pop
ldc 996
pop
ldc 997
pop
ldc 998
pop
ldc 999
pop
ldc 1000
pop
ldc 1001
pop
ldc 1002
pop
ldc 1003
pop
ldc 1004
pop
ldc 1005
pop
ldc 1006
pop
ldc 1007
pop
ldc 1008
pop
ldc 1009
pop
ldc 1010
pop
ldc 1011
pop
ldc 1012
pop
ldc 1013
pop
ldc 1014
pop
ldc 1015
pop
ldc 1016
pop
ldc 1017
pop
ldc 1018
pop
ldc 1019
pop
ldc 1020
pop
ldc 1021
pop
ldc 1022
pop
ldc 1023
pop
ldc 1024
pop
ldc 1025
pop
ldc 1026
pop
ldc 1027
pop
ldc 1028
pop
ldc 1029
pop
ldc 1030
pop
ldc 1031
pop
ldc 1032
pop
ldc 1033
pop
ldc 1034
pop
ldc 1035
pop
ldc 1036
pop
ldc 1037
pop
ldc 1038
pop
ldc 1039
pop
ldc 1040
pop
ldc 1041
pop
ldc 1042
pop
ldc 1043
pop
ldc 1044
pop
ldc 1045
pop
ldc 1046
pop
ldc 1047
pop
ldc 1048
pop
ldc 1049
pop
ldc 1050
pop
ldc 1051
pop
ldc 1052
pop
ldc 1053
pop
ldc 1054
pop
ldc 1055
pop
ldc 1056
pop
ldc 1057
pop
ldc 1058
pop
ldc 1059
pop
ldc 1060
pop
ldc 1061
pop
ldc 1062
pop
ldc 1063
pop
ldc 1064
pop
ldc 1065
pop
ldc 1066
pop
ldc 1067
pop
ldc 1068
pop
ldc 1069
pop
ldc 1070
pop
ldc 1071
pop
ldc 1072
pop
ldc 1073
pop
ldc 1074
pop
ldc 1075
pop
ldc 1076
pop
ldc 1077
pop
ldc 1078
pop
ldc 1079
pop
ldc 1080
pop
ldc 1081
pop
ldc 1082
pop
ldc 1083
pop
ldc 1084
pop
ldc 1085
pop
ldc 1086
pop
ldc 1087
pop
ldc 1088
pop
ldc 1089
pop
ldc 1090
pop
ldc 1091
pop
ldc 1092
pop
ldc 1093
pop
ldc 1094
pop
ldc 1095
pop
ldc 1096
pop
ldc 1097
pop
ldc 1098
pop
ldc 1099
pop
ldc 1100
pop
ldc 1101
pop
ldc 1102
pop
ldc 1103
pop
ldc 1104
pop
ldc 1105
pop
ldc 1106
pop
ldc 1107
pop
ldc 1108
pop
ldc 1109
pop
ldc 1110
pop
ldc 1111
pop
ldc 1112
pop
ldc 1113
pop
ldc 1114
pop
ldc 1115
pop
ldc 1116
pop
ldc 1117
pop
ldc 1118
pop
ldc 1119
pop
ldc 1120
pop
ldc 1121
pop
ldc 1122
pop
ldc 1123
pop
ldc 1124
pop
ldc 1125
pop
ldc 1126
pop
ldc 1127
pop
ldc 1128
pop
ldc 1129
pop
ldc 1130
pop
ldc 1131
pop
ldc 1132
pop
ldc 1133
pop
ldc 1134
pop
ldc 1135
pop
ldc 1136
pop
ldc 1137
pop
ldc 1138
pop
ldc 1139
pop
ldc 1140
pop
ldc 1141
pop
ldc 1142
pop
ldc 1143
pop
ldc 1144
pop
ldc 1145
pop
ldc 1146
pop
ldc 1147
pop
ldc 1148
pop
ldc 1149
pop
ldc 1150
pop
ldc 1151
pop
ldc 1152
pop
ldc 1153
pop
ldc 1154
pop
ldc 1155
pop
ldc 1156
pop
ldc 1157
pop
ldc 1158
pop
ldc 1159
pop
ldc 1160
pop
ldc 1161
pop
ldc 1162
pop
ldc 1163
pop
ldc 1164
pop
ldc 1165
pop
ldc 1166
pop
ldc 1167
pop
ldc 1168
pop
ldc 1169
pop
ldc 1170
pop
ldc 1171
pop
ldc 1172
pop
ldc 1173
pop
ldc 1174
pop
ldc 1175
pop
ldc 1176
pop
ldc 1177
pop
ldc 1178
pop
ldc 1179
pop
ldc 1180
pop
ldc 1181
pop
ldc 1182
pop
ldc 1183
pop
ldc 1184
pop
ldc 1185
pop
ldc 1186
pop
ldc 1187
pop
ldc 1188
pop
ldc 1189
pop
ldc 1190
pop
ldc 1191
pop
ldc 1192
pop
ldc 1193
pop
ldc 1194
pop
ldc 1195
pop
ldc 1196
pop
ldc 1197
pop
ldc 1198
pop
ldc 1199
pop
ldc 1200
pop
ldc 1201
pop
ldc 1202
pop
ldc 1203
pop
ldc 1204
pop
ldc 1205
pop
ldc 1206
pop
ldc 1207
pop
ldc 1208
pop
ldc 1209
pop
ldc 1210
pop
ldc 1211
pop
ldc 1212
pop
ldc 1213
pop
ldc 1214
pop
ldc 1215
pop
ldc 1216
pop
ldc 1217
pop
ldc 1218
pop
ldc 1219
pop
ldc 1220
pop
ldc 1221
pop
ldc 1222
pop
ldc 1223
pop
ldc 1224
pop
ldc 1225
pop
ldc 1226
pop
ldc 1227
pop
ldc 1228
pop
ldc 1229
pop
ldc 1230
pop
ldc 1231
pop
ldc 1232
pop
ldc 1233
pop
ldc 1234
pop
ldc 1235
pop
ldc 1236
pop
ldc 1237
pop
ldc 1238
pop
ldc 1239
pop
ldc 1240
pop
ldc 1241
pop
ldc 1242
pop
ldc 1243
pop
ldc 1244
pop
ldc 1245
pop
ldc 1246
pop
ldc 1247
pop
ldc 1248
pop
ldc 1249
pop
ldc 1250
pop
ldc 1251
pop
ldc 1252
pop
ldc 1253
pop
ldc 1254
pop
ldc 1255
pop
ldc 1256
pop
ldc 1257
pop
ldc 1258
pop
ldc 1259
pop
ldc 1260
pop
ldc 1261
pop
ldc 1262
pop
ldc 1263
pop
ldc 1264
pop
ldc 1265
pop
ldc 1266
pop
ldc 1267
pop
ldc 1268
pop
ldc 1269
pop
ldc 1270
pop
ldc 1271
pop
ldc 1272
pop
ldc 1273
pop
ldc 1274
pop
ldc 1275
pop
ldc 1276
pop
ldc 1277
pop
ldc 1278
pop
ldc 1279
pop
ldc 1280
pop
ldc 1281
pop
ldc 1282
pop
ldc 1283
pop
ldc 1284
pop
ldc 1285
pop
ldc 1286
pop
ldc 1287
pop
ldc 1288
pop
ldc 1289
pop
ldc 1290
pop
ldc 1291
pop
ldc 1292
pop
ldc 1293
pop
ldc 1294
pop
ldc 1295
pop
ldc 1296
pop
ldc 1297
pop
ldc 1298
pop
ldc 1299
pop
ldc 1300
pop
ldc 1301
pop
ldc 1302
pop
ldc 1303
pop
ldc 1304
pop
ldc 1305
pop
ldc 1306
pop
ldc 1307
pop
ldc 1308
pop
ldc 1309
pop
ldc 1310
pop
ldc 1311
pop
ldc 1312
pop
ldc 1313
pop
ldc 1314
pop
ldc 1315
pop
ldc 1316
pop
ldc 1317
pop
ldc 1318
pop
ldc 1319
pop
ldc 1320
pop
ldc 1321
pop
ldc 1322
pop
ldc 1323
pop
ldc 1324
pop
ldc 1325
pop
ldc 1326
pop
ldc 1327
pop
ldc 1328
pop
ldc 1329
pop
ldc 1330
pop
ldc 1331
pop
ldc 1332
pop
ldc 1333
pop
ldc 1334
pop
ldc 1335
pop
ldc 1336
pop
ldc 1337
pop
ldc 1338
pop
ldc 1339
pop
ldc 1340
pop
ldc 1341
pop
ldc 1342
pop
ldc 1343
pop
ldc 1344
pop
ldc 1345
pop
ldc 1346
pop
ldc 1347
pop
ldc 1348
pop
ldc 1349
pop
ldc 1350
pop
ldc 1351
pop
ldc 1352
pop
ldc 1353
pop
ldc 1354
pop
ldc 1355
pop
ldc 1356
pop
ldc 1357
pop
ldc 1358
pop
ldc 1359
pop
ldc 1360
pop
ldc 1361
pop
ldc 1362
pop
ldc 1363
pop
ldc 1364
pop
ldc 1365
pop
ldc 1366
pop
ldc 1367
pop
ldc 1368
pop
ldc 1369
pop
ldc 1370
pop
ldc 1371
pop
ldc 1372
pop
ldc 1373
pop
ldc 1374
pop
ldc 1375
pop
ldc 1376
pop
ldc 1377
pop
ldc 1378
pop
ldc 1379
pop
ldc 1380
pop
ldc 1381
pop
ldc 1382
pop
ldc 1383
pop
ldc 1384
pop
ldc 1385
pop
ldc 1386
pop
ldc 1387
pop
ldc 1388
pop
ldc 1389
pop
ldc 1390
pop
ldc 1391
pop
ldc 1392
pop
ldc 1393
pop
ldc 1394
pop
ldc 1395
pop
ldc 1396
pop
ldc 1397
pop
ldc 1398
pop
ldc 1399
pop
ldc 1400
pop
ldc 1401
pop
ldc 1402
pop
ldc 1403
pop
ldc 1404
pop
ldc 1405
pop
ldc 1406
pop
ldc 1407
pop
ldc 1408
pop
ldc 1409
pop
ldc 1410
pop
ldc 1411
pop
ldc 1412
pop
ldc 1413
pop
ldc 1414
pop
ldc 1415
pop
ldc 1416
pop
ldc 1417
pop
ldc 1418
pop
ldc 1419
pop
ldc 1420
pop
ldc 1421
pop
ldc 1422
pop
ldc 1423
pop
ldc 1424
pop
ldc 1425
pop
ldc 1426
pop
ldc 1427
pop
ldc 1428
pop
ldc 1429
pop
ldc 1430
pop
ldc 1431
pop
ldc 1432
pop
ldc 1433
pop
ldc 1434
pop
ldc 1435
pop
ldc 1436
pop
ldc 1437
pop
ldc 1438
pop
ldc 1439
pop
ldc 1440
pop
ldc 1441
pop
ldc 1442
pop
ldc 1443
pop
ldc 1444
pop
ldc 1445
pop
ldc 1446
pop
ldc 1447
pop
ldc 1448
pop
ldc 1449
pop
ldc 1450
pop
ldc 1451
pop
ldc 1452
pop
ldc 1453
pop
ldc 1454
pop
ldc 1455
pop
ldc 1456
pop
ldc 1457
pop
ldc 1458
pop
ldc 1459
pop
ldc 1460
pop
ldc 1461
pop
ldc 1462
pop
ldc 1463
pop
ldc 1464
pop
ldc 1465
pop
ldc 1466
pop
ldc 1467
pop
ldc 1468
pop
ldc 1469
pop
ldc 1470
pop
ldc 1471
pop
ldc 1472
pop
ldc 1473
pop
ldc 1474
pop
ldc 1475
pop
ldc 1476
pop
ldc 1477
pop
ldc 1478
pop
ldc 1479
pop
ldc 1480
pop
ldc 1481
pop
ldc 1482
pop
ldc 1483
pop
ldc 1484
pop
ldc 1485
pop
ldc 1486
pop
ldc 1487
pop
ldc 1488
pop
ldc 1489
pop
ldc 1490
pop
ldc 1491
pop
ldc 1492
pop
ldc 1493
pop
ldc 1494
pop
ldc 1495
pop
ldc 1496
pop
ldc 1497
pop
ldc 1498
pop
ldc 1499
pop
ldc 1500
pop
ldc 1501
pop
ldc 1502
pop
ldc 1503
pop
ldc 1504
pop
ldc 1505
pop
ldc 1506
pop
ldc 1507
pop
ldc 1508
pop
ldc 1509
pop
ldc 1510
pop
ldc 1511
pop
ldc 1512
pop
ldc 1513
pop
ldc 1514
pop
ldc 1515
pop
ldc 1516
pop
ldc 1517
pop
ldc 1518
pop
ldc 1519
pop
ldc 1520
pop
ldc 1521
pop
ldc 1522
pop
ldc 1523
pop
ldc 1524
pop
ldc 1525
pop
ldc 1526
pop
ldc 1527
pop
ldc 1528
pop
ldc 1529
pop
ldc 1530
pop
ldc 1531
pop
ldc 1532
pop
ldc 1533
pop
ldc 1534
pop
ldc 1535
pop
ldc 1536
pop
ldc 1537
pop
ldc 1538
pop
ldc 1539
pop
ldc 1540
pop
ldc 1541
pop
ldc 1542
pop
ldc 1543
pop
ldc 1544
pop
ldc 1545
pop
ldc 1546
pop
ldc 1547
pop
ldc 1548
pop
ldc 1549
pop
ldc 1550
pop
ldc 1551
pop
ldc 1552
pop
ldc 1553
pop
ldc 1554
pop
ldc 1555
pop
ldc 1556
pop
ldc 1557
pop
ldc 1558
pop
ldc 1559
pop
ldc 1560
pop
ldc 1561
pop
ldc 1562
pop
ldc 1563
pop
ldc 1564
pop
ldc 1565
pop
ldc 1566
pop
ldc 1567
pop
ldc 1568
pop
ldc 1569
pop
ldc 1570
pop
ldc 1571
pop
ldc 1572
pop
ldc 1573
pop
ldc 1574
pop
ldc 1575
pop
ldc 1576
pop
ldc 1577
pop
ldc 1578
pop
ldc 1579
pop
ldc 1580
pop
ldc 1581
pop
ldc 1582
pop
ldc 1583
pop
ldc 1584
pop
ldc 1585
pop
ldc 1586
pop
ldc 1587
pop
ldc 1588
pop
ldc 1589
pop
ldc 1590
pop
ldc 1591
pop
ldc 1592
pop
ldc 1593
pop
ldc 1594
pop
ldc 1595
pop
ldc 1596
pop
ldc 1597
pop
ldc 1598
pop
ldc 1599
pop
ldc 1600
pop
ldc 1601
pop
ldc 1602
pop
ldc 1603
pop
ldc 1604
pop
ldc 1605
pop
ldc 1606
pop
ldc 1607
pop
ldc 1608
pop
ldc 1609
pop
ldc 1610
pop
ldc 1611
pop
ldc 1612
pop
ldc 1613
pop
ldc 1614
pop
ldc 1615
pop
ldc 1616
pop
ldc 1617
pop
ldc 1618
pop
ldc 1619
pop
ldc 1620
pop
ldc 1621
pop
ldc 1622
pop
ldc 1623
pop
ldc 1624
pop
ldc 1625
pop
ldc 1626
pop
ldc 1627
pop
ldc 1628
pop
ldc 1629
pop
ldc 1630
pop
ldc 1631
pop
ldc 1632
pop
ldc 1633
pop
ldc 1634
pop
ldc 1635
pop
ldc 1636
pop
ldc 1637
pop
ldc 1638
pop
ldc 1639
pop
ldc 1640
pop
ldc 1641
pop
ldc 1642
pop
ldc 1643
pop
ldc 1644
pop
ldc 1645
pop
ldc 1646
pop
ldc 1647
pop
ldc 1648
pop
ldc 1649
pop
ldc 1650
pop
ldc 1651
pop
ldc 1652
pop
ldc 1653
pop
ldc 1654
pop
ldc 1655
pop
ldc 1656
pop
ldc 1657
pop
ldc 1658
pop
ldc 1659
pop
ldc 1660
pop
ldc 1661
pop
ldc 1662
pop
ldc 1663
pop
ldc 1664
pop
ldc 1665
pop
ldc 1666
pop
ldc 1667
pop
ldc 1668
pop
ldc 1669
pop
ldc 1670
pop
ldc 1671
pop
ldc 1672
pop
ldc 1673
pop
ldc 1674
pop
ldc 1675
pop
ldc 1676
pop
ldc 1677
pop
ldc 1678
pop
ldc 1679
pop
ldc 1680
pop
ldc 1681
pop
ldc 1682
pop
ldc 1683
pop
ldc 1684
pop
ldc 1685
pop
ldc 1686
pop
ldc 1687
pop
ldc 1688
pop
ldc 1689
pop
ldc 1690
pop
ldc 1691
pop
ldc 1692
pop
ldc 1693
pop
ldc 1694
pop
ldc 1695
pop
ldc 1696
pop
ldc 1697
pop
ldc 1698
pop
ldc 1699
pop
ldc 1700
pop
ldc 1701
pop
ldc 1702
pop
ldc 1703
pop
ldc 1704
pop
ldc 1705
pop
ldc 1706
pop
ldc 1707
pop
ldc 1708
pop
ldc 1709
pop
ldc 1710
pop
ldc 1711
pop
ldc 1712
pop
ldc 1713
pop
ldc 1714
pop
ldc 1715
pop
ldc 1716
pop
ldc 1717
pop
ldc 1718
pop
ldc 1719
pop
ldc 1720
pop
ldc 1721
pop
ldc 1722
pop
ldc 1723
pop
ldc 1724
pop
ldc 1725
pop
ldc 1726
pop
ldc 1727
pop
ldc 1728
pop
ldc 1729
pop
ldc 1730
pop
ldc 1731
pop
ldc 1732
pop
ldc 1733
pop
ldc 1734
pop
ldc 1735
pop
ldc 1736
pop
ldc 1737
pop
ldc 1738
pop
ldc 1739
pop
ldc 1740
pop
ldc 1741
pop
ldc 1742
pop
ldc 1743
pop
ldc 1744
pop
ldc 1745
pop
ldc 1746
pop
ldc 1747
pop
ldc 1748
pop
ldc 1749
pop
ldc 1750
pop
ldc 1751
pop
ldc 1752
pop
ldc 1753
pop
ldc 1754
pop
ldc 1755
pop
ldc 1756
pop
ldc 1757
pop
ldc 1758
pop
ldc 1759
pop
ldc 1760
pop
ldc 1761
pop
ldc 1762
pop
ldc 1763
pop
ldc 1764
pop
ldc 1765
pop
ldc 1766
pop
ldc 1767
pop
ldc 1768
pop
ldc 1769
pop
ldc 1770
pop
ldc 1771
pop
ldc 1772
pop
ldc 1773
pop
ldc 1774
pop
ldc 1775
pop
ldc 1776
pop
ldc 1777
pop
ldc 1778
pop
ldc 1779
pop
ldc 1780
pop
ldc 1781
pop
ldc 1782
pop
ldc 1783
pop
ldc 1784
pop
ldc 1785
pop
ldc 1786
pop
ldc 1787
pop
ldc 1788
pop
ldc 1789
pop
ldc 1790
pop
ldc 1791
pop
ldc 1792
pop
ldc 1793
pop
ldc 1794
pop
ldc 1795
pop
ldc 1796
pop
ldc 1797
pop
ldc 1798
pop
ldc 1799
pop
ldc 1800
pop
ldc 1801
pop
ldc 1802
pop
ldc 1803
pop
ldc 1804
pop
ldc 1805
pop
ldc 1806
pop
ldc 1807
pop
ldc 1808
pop
ldc 1809
pop
ldc 1810
pop
ldc 1811
pop
ldc 1812
pop
ldc 1813
pop
ldc 1814
pop
ldc 1815
pop
ldc 1816
pop
ldc 1817
pop
ldc 1818
pop
ldc 1819
pop
ldc 1820
pop
ldc 1821
pop
ldc 1822
pop
ldc 1823
pop
ldc 1824
pop
ldc 1825
pop
ldc 1826
pop
ldc 1827
pop
ldc 1828
pop
ldc 1829
pop
ldc 1830
pop
ldc 1831
pop
ldc 1832
pop
ldc 1833
pop
ldc 1834
pop
ldc 1835
pop
ldc 1836
pop
ldc 1837
pop
ldc 1838
pop
ldc 1839
pop
ldc 1840
pop
ldc 1841
pop
ldc 1842
pop
ldc 1843
pop
ldc 1844
pop
ldc 1845
pop
ldc 1846
pop
ldc 1847
pop
ldc 1848
pop
ldc 1849
pop
ldc 1850
pop
ldc 1851
pop
ldc 1852
pop
ldc 1853
pop
ldc 1854
pop
ldc 1855
pop
ldc 1856
pop
ldc 1857
pop
ldc 1858
pop
ldc 1859
pop
ldc 1860
pop
ldc 1861
pop
ldc 1862
pop
ldc 1863
pop
ldc 1864
pop
ldc 1865
pop
ldc 1866
pop
ldc 1867
pop
ldc 1868
pop
ldc 1869
pop
ldc 1870
pop
ldc 1871
pop
ldc 1872
pop
ldc 1873
pop
ldc 1874
pop
ldc 1875
pop
ldc 1876
pop
ldc 1877
pop
ldc 1878
pop
ldc 1879
pop
ldc 1880
pop
ldc 1881
pop
ldc 1882
pop
ldc 1883
pop
ldc 1884
pop
ldc 1885
pop
ldc 1886
pop
ldc 1887
pop
ldc 1888
pop
ldc 1889
pop
ldc 1890
pop
ldc 1891
pop
ldc 1892
pop
ldc 1893
pop
ldc 1894
pop
ldc 1895
pop
ldc 1896
pop
ldc 1897
pop
ldc 1898
pop
ldc 1899
pop
ldc 1900
pop
ldc 1901
pop
ldc 1902
pop
ldc 1903
pop
ldc 1904
pop
ldc 1905
pop
ldc 1906
pop
ldc 1907
pop
ldc 1908
pop
ldc 1909
pop
ldc 1910
pop
ldc 1911
pop
ldc 1912
pop
ldc 1913
pop
ldc 1914
pop
ldc 1915
pop
ldc 1916
pop
ldc 1917
pop
ldc 1918
pop
ldc 1919
pop
ldc 1920
pop
ldc 1921
pop
ldc 1922
pop
ldc 1923
pop
ldc 1924
pop
ldc 1925
pop
ldc 1926
pop
ldc 1927
pop
ldc 1928
pop
ldc 1929
pop
ldc 1930
pop
ldc 1931
pop
ldc 1932
pop
ldc 1933
pop
ldc 1934
pop
ldc 1935
pop
ldc 1936
pop
ldc 1937
pop
ldc 1938
pop
ldc 1939
pop
ldc 1940
pop
ldc 1941
pop
ldc 1942
pop
ldc 1943
pop
ldc 1944
pop
ldc 1945
pop
ldc 1946
pop
ldc 1947
pop
ldc 1948
pop
ldc 1949
pop
ldc 1950
pop
ldc 1951
pop
ldc 1952
pop
ldc 1953
pop
ldc 1954
pop
ldc 1955
pop
ldc 1956
pop
ldc 1957
pop
ldc 1958
pop
ldc 1959
pop
ldc 1960
pop
ldc 1961
pop
ldc 1962
pop
ldc 1963
pop
ldc 1964
pop
ldc 1965
pop
ldc 1966
pop
ldc 1967
pop
ldc 1968
pop
ldc 1969
pop
ldc 1970
pop
ldc 1971
pop
ldc 1972
pop
ldc 1973
pop
ldc 1974
pop
ldc 1975
pop
ldc 1976
pop
ldc 1977
pop
ldc 1978
pop
ldc 1979
pop
ldc 1980
pop
ldc 1981
pop
ldc 1982
pop
ldc 1983
pop
ldc 1984
pop
ldc 1985
pop
ldc 1986
pop
ldc 1987
pop
ldc 1988
pop
ldc 1989
pop
ldc 1990
pop
ldc 1991
pop
ldc 1992
pop
ldc 1993
pop
ldc 1994
pop
ldc 1995
pop
ldc 1996
pop
ldc 1997
pop
ldc 1998
pop
ldc 1999
pop
ldc 2000
pop
ldc 2001
pop
ldc 2002
pop
ldc 2003
pop
ldc 2004
pop
ldc 2005
pop
ldc 2006
pop
ldc 2007
pop
ldc 2008
pop
ldc 2009
pop
ldc 2010
pop
ldc 2011
pop
ldc 2012
pop
ldc 2013
pop
ldc 2014
pop
ldc 2015
pop
ldc 2016
pop
ldc 2017
pop
ldc 2018
pop
ldc 2019
pop
ldc 2020
pop
ldc 2021
pop
ldc 2022
pop
ldc 2023
pop
ldc 2024
pop
ldc 2025
pop
ldc 2026
pop
ldc 2027
pop
ldc 2028
pop
ldc 2029
pop
ldc 2030
pop
ldc 2031
pop
ldc 2032
pop
ldc 2033
pop
ldc 2034
pop
ldc 2035
pop
ldc 2036
pop
ldc 2037
pop
ldc 2038
pop
ldc 2039
pop
ldc 2040
pop
ldc 2041
pop
ldc 2042
pop
ldc 2043
pop
ldc 2044
pop
ldc 2045
pop
ldc 2046
pop
ldc 2047
pop
ldc 2048
pop
ldc 2049
pop
ldc 2050
pop
ldc 2051
pop
ldc 2052
pop
ldc 2053
pop
ldc 2054
pop
ldc 2055
pop
ldc 2056
pop
ldc 2057
pop
ldc 2058
pop
ldc 2059
pop
ldc 2060
pop
ldc 2061
pop
ldc 2062
pop
ldc 2063
pop
ldc 2064
pop
ldc 2065
pop
ldc 2066
pop
ldc 2067
pop
ldc 2068
pop
ldc 2069
pop
ldc 2070
pop
ldc 2071
pop
ldc 2072
pop
ldc 2073
pop
ldc 2074
pop
ldc 2075
pop
ldc 2076
pop
ldc 2077
pop
ldc 2078
pop
ldc 2079
pop
ldc 2080
pop
ldc 2081
pop
ldc 2082
pop
ldc 2083
pop
ldc 2084
pop
ldc 2085
pop
ldc 2086
pop
ldc 2087
pop
ldc 2088
pop
ldc 2089
pop
ldc 2090
pop
ldc 2091
pop
ldc 2092
pop
ldc 2093
pop
ldc 2094
pop
ldc 2095
pop
ldc 2096
pop
ldc 2097
pop
ldc 2098
pop
ldc 2099
pop
ldc 2100
pop
ldc 2101
pop
ldc 2102
pop
ldc 2103
pop
ldc 2104
pop
ldc 2105
pop
ldc 2106
pop
ldc 2107
pop
ldc 2108
pop
ldc 2109
pop
ldc 2110
pop
ldc 2111
pop
ldc 2112
pop
ldc 2113
pop
ldc 2114
pop
ldc 2115
pop
ldc 2116
pop
ldc 2117
pop
ldc 2118
pop
ldc 2119
pop
ldc 2120
pop
ldc 2121
pop
ldc 2122
pop
ldc 2123
pop
ldc 2124
pop
ldc 2125
pop
ldc 2126
pop
ldc 2127
pop
ldc 2128
pop
ldc 2129
pop
ldc 2130
pop
ldc 2131
pop
ldc 2132
pop
ldc 2133
pop
ldc 2134
pop
ldc 2135
pop
ldc 2136
pop
ldc 2137
pop
ldc 2138
pop
ldc 2139
pop
ldc 2140
pop
ldc 2141
pop
ldc 2142
pop
ldc 2143
pop
ldc 2144
pop
ldc 2145
pop
ldc 2146
pop
ldc 2147
pop
ldc 2148
pop
ldc 2149
pop
ldc 2150
pop
ldc 2151
pop
ldc 2152
pop
ldc 2153
pop
ldc 2154
pop
ldc 2155
pop
ldc 2156
pop
ldc 2157
pop
ldc 2158
pop
ldc 2159
pop
ldc 2160
pop
ldc 2161
pop
ldc 2162
pop
ldc 2163
pop
ldc 2164
pop
ldc 2165
pop
ldc 2166
pop
ldc 2167
pop
ldc 2168
pop
ldc 2169
pop
ldc 2170
pop
ldc 2171
pop
ldc 2172
pop
ldc 2173
pop
ldc 2174
pop
ldc 2175
pop
ldc 2176
pop
ldc 2177
pop
ldc 2178
pop
ldc 2179
pop
ldc 2180
pop
ldc 2181
pop
ldc 2182
pop
ldc 2183
pop
ldc 2184
pop
ldc 2185
pop
ldc 2186
pop
ldc 2187
pop
ldc 2188
pop
ldc 2189
pop
ldc 2190
pop
ldc 2191
pop
ldc 2192
pop
ldc 2193
pop
ldc 2194
pop
ldc 2195
pop
ldc 2196
pop
ldc 2197
pop
ldc 2198
pop
ldc 2199
pop
ldc 2200
pop
ldc 2201
pop
ldc 2202
pop
ldc 2203
pop
ldc 2204
pop
ldc 2205
pop
ldc 2206
pop
ldc 2207
pop
ldc 2208
pop
ldc 2209
pop
ldc 2210
pop
ldc 2211
pop
ldc 2212
pop
ldc 2213
pop
ldc 2214
pop
ldc 2215
pop
ldc 2216
pop
ldc 2217
pop
ldc 2218
pop
ldc 2219
pop
ldc 2220
pop
ldc 2221
pop
ldc 2222
pop
ldc 2223
pop
ldc 2224
pop
ldc 2225
pop
ldc 2226
pop
ldc 2227
pop
ldc 2228
pop
ldc 2229
pop
ldc 2230
pop
ldc 2231
pop
ldc 2232
pop
ldc 2233
pop
ldc 2234
pop
ldc 2235
pop
ldc 2236
pop
ldc 2237
pop
ldc 2238
pop
ldc 2239
pop
ldc 2240
pop
ldc 2241
pop
ldc 2242
pop
ldc 2243
pop
ldc 2244
pop
ldc 2245
pop
ldc 2246
pop
ldc 2247
pop
ldc 2248
pop
ldc 2249
pop
ldc 2250
pop
ldc 2251
pop
ldc 2252
pop
ldc 2253
pop
ldc 2254
pop
ldc 2255
pop
ldc 2256
pop
ldc 2257
pop
ldc 2258
pop
ldc 2259
pop
ldc 2260
pop
ldc 2261
pop
ldc 2262
pop
ldc 2263
pop
ldc 2264
pop
ldc 2265
pop
ldc 2266
pop
ldc 2267
pop
ldc 2268
pop
ldc 2269
pop
ldc 2270
pop
ldc 2271
pop
ldc 2272
pop
ldc 2273
pop
ldc 2274
pop
ldc 2275
pop
ldc 2276
pop
ldc 2277
pop
ldc 2278
pop
ldc 2279
pop
ldc 2280
pop
ldc 2281
pop
ldc 2282
pop
ldc 2283
pop
ldc 2284
pop
ldc 2285
pop
ldc 2286
pop
ldc 2287
pop
ldc 2288
pop
ldc 2289
pop
ldc 2290
pop
ldc 2291
pop
ldc 2292
pop
ldc 2293
pop
ldc 2294
pop
ldc 2295
pop
ldc 2296
pop
ldc 2297
pop
ldc 2298
pop
ldc 2299
pop
ldc 2300
pop
ldc 2301
pop
ldc 2302
pop
ldc 2303
pop
ldc 2304
pop
ldc 2305
pop
ldc 2306
pop
ldc 2307
pop
ldc 2308
pop
ldc 2309
pop
ldc 2310
pop
ldc 2311
pop
ldc 2312
pop
ldc 2313
pop
ldc 2314
pop
ldc 2315
pop
ldc 2316
pop
ldc 2317
pop
ldc 2318
pop
ldc 2319
pop
ldc 2320
pop
ldc 2321
pop
ldc 2322
pop
ldc 2323
pop
ldc 2324
pop
ldc 2325
pop
ldc 2326
pop
ldc 2327
pop
ldc 2328
pop
ldc 2329
pop
ldc 2330
pop
ldc 2331
pop
ldc 2332
pop
ldc 2333
pop
ldc 2334
pop
ldc 2335
pop
ldc 2336
pop
ldc 2337
pop
ldc 2338
pop
ldc 2339
pop
ldc 2340
pop
ldc 2341
pop
ldc 2342
pop
ldc 2343
pop
ldc 2344
pop
ldc 2345
pop
ldc 2346
pop
ldc 2347
pop
ldc 2348
pop
ldc 2349
pop
ldc 2350
pop
ldc 2351
pop
ldc 2352
pop
ldc 2353
pop
ldc 2354
pop
ldc 2355
pop
ldc 2356
pop
ldc 2357
pop
ldc 2358
pop
ldc 2359
pop
ldc 2360
pop
ldc 2361
pop
ldc 2362
pop
ldc 2363
pop
ldc 2364
pop
ldc 2365
pop
ldc 2366
pop
ldc 2367
pop
ldc 2368
pop
ldc 2369
pop
ldc 2370
pop
ldc 2371
pop
ldc 2372
pop
ldc 2373
pop
ldc 2374
pop
ldc 2375
pop
ldc 2376
pop
ldc 2377
pop
ldc 2378
pop
ldc 2379
pop
ldc 2380
pop
ldc 2381
pop
ldc 2382
pop
ldc 2383
pop
ldc 2384
pop
ldc 2385
pop
ldc 2386
pop
ldc 2387
pop
ldc 2388
pop
ldc 2389
pop
ldc 2390
pop
ldc 2391
pop
ldc 2392
pop
ldc 2393
pop
ldc 2394
pop
ldc 2395
pop
ldc 2396
pop
ldc 2397
pop
ldc 2398
pop
ldc 2399
pop
ldc 2400
pop
ldc 2401
pop
ldc 2402
pop
ldc 2403
pop
ldc 2404
pop
ldc 2405
pop
ldc 2406
pop
ldc 2407
pop
ldc 2408
pop
ldc 2409
pop
ldc 2410
pop
ldc 2411
pop
ldc 2412
pop
ldc 2413
pop
ldc 2414
pop
ldc 2415
pop
ldc 2416
pop
ldc 2417
pop
ldc 2418
pop
ldc 2419
pop
ldc 2420
pop
ldc 2421
pop
ldc 2422
pop
ldc 2423
pop
ldc 2424
pop
ldc 2425
pop
ldc 2426
pop
ldc 2427
pop
ldc 2428
pop
ldc 2429
pop
ldc 2430
pop
ldc 2431
pop
ldc 2432
pop
ldc 2433
pop
ldc 2434
pop
ldc 2435
pop
ldc 2436
pop
ldc 2437
pop
ldc 2438
pop
ldc 2439
pop
ldc 2440
pop
ldc 2441
pop
ldc 2442
pop
ldc 2443
pop
ldc 2444
pop
ldc 2445
pop
ldc 2446
pop
ldc 2447
pop
ldc 2448
pop
ldc 2449
pop
ldc 2450
pop
ldc 2451
pop
ldc 2452
pop
ldc 2453
pop
ldc 2454
pop
ldc 2455
pop
ldc 2456
pop
ldc 2457
pop
ldc 2458
pop
ldc 2459
pop
ldc 2460
pop
ldc 2461
pop
ldc 2462
pop
ldc 2463
pop
ldc 2464
pop
ldc 2465
pop
ldc 2466
pop
ldc 2467
pop
ldc 2468
pop
ldc 2469
pop
ldc 2470
pop
ldc 2471
pop
ldc 2472
pop
ldc 2473
pop
ldc 2474
pop
ldc 2475
pop
ldc 2476
pop
ldc 2477
pop
ldc 2478
pop
ldc 2479
pop
ldc 2480
pop
ldc 2481
pop
ldc 2482
pop
ldc 2483
pop
ldc 2484
pop
ldc 2485
pop
ldc 2486
pop
ldc 2487
pop
ldc 2488
pop
ldc 2489
pop
ldc 2490
pop
ldc 2491
pop
ldc 2492
pop
ldc 2493
pop
ldc 2494
pop
ldc 2495
pop
ldc 2496
pop
ldc 2497
pop
ldc 2498
pop
ldc 2499
pop
ldc 2500
pop
ldc 2501
pop
ldc 2502
pop
ldc 2503
pop
ldc 2504
pop
ldc 2505
pop
ldc 2506
pop
ldc 2507
pop
ldc 2508
pop
ldc 2509
pop
ldc 2510
pop
ldc 2511
pop
ldc 2512
pop
ldc 2513
pop
ldc 2514
pop
ldc 2515
pop
ldc 2516
pop
ldc 2517
pop
ldc 2518
pop
ldc 2519
pop
ldc 2520
pop
ldc 2521
pop
ldc 2522
pop
ldc 2523
pop
ldc 2524
pop
ldc 2525
pop
ldc 2526
pop
ldc 2527
pop
ldc 2528
pop
ldc 2529
pop
ldc 2530
pop
ldc 2531
pop
ldc 2532
pop
ldc 2533
pop
ldc 2534
pop
ldc 2535
pop
ldc 2536
pop
ldc 2537
pop
ldc 2538
pop
ldc 2539
pop
ldc 2540
pop
ldc 2541
pop
ldc 2542
pop
ldc 2543
pop
ldc 2544
pop
ldc 2545
pop
ldc 2546
pop
ldc 2547
pop
ldc 2548
pop
ldc 2549
pop
ldc 2550
pop
ldc 2551
pop
ldc 2552
pop
ldc 2553
pop
ldc 2554
pop
ldc 2555
pop
ldc 2556
pop
ldc 2557
pop
ldc 2558
pop
ldc 2559
pop
ldc 2560
pop
ldc 2561
pop
ldc 2562
pop
ldc 2563
pop
ldc 2564
pop
ldc 2565
pop
ldc 2566
pop
ldc 2567
pop
ldc 2568
pop
ldc 2569
pop
ldc 2570
pop
ldc 2571
pop
ldc 2572
pop
ldc 2573
pop
ldc 2574
pop
ldc 2575
pop
ldc 2576
pop
ldc 2577
pop
ldc 2578
pop
ldc 2579
pop
ldc 2580
pop
ldc 2581
pop
ldc 2582
pop
ldc 2583
pop
ldc 2584
pop
ldc 2585
pop
ldc 2586
pop
ldc 2587
pop
ldc 2588
pop
ldc 2589
pop
ldc 2590
pop
ldc 2591
pop
ldc 2592
pop
ldc 2593
pop
ldc 2594
pop
ldc 2595
pop
ldc 2596
pop
ldc 2597
pop
ldc 2598
pop
ldc 2599
pop
ldc 2600
pop
ldc 2601
pop
ldc 2602
pop
ldc 2603
pop
ldc 2604
pop
ldc 2605
pop
ldc 2606
pop
   
   ldc2_w -9223372036854775808     ; push -9223372036854775808 to stack 
   dup2   
   lcmp
   iconst_0
   if_icmpne J1  ; compare two values on the top of stack
                 ; if values are equal test pass,else test fail
   sipush 104
   ireturn

   astore_1
J1: 
   sipush 105
   ireturn

   return
.end method

;
; standard main function
.method public static main([Ljava/lang/String;)V
  .limit stack 2
  .limit locals 1

  new org/apache/harmony/vts/test/vm/jvms/instructions/loadStore/ldc2_w/ldc2_w05/ldc2_w0502/ldc2_w0502p
  dup
  invokespecial org/apache/harmony/vts/test/vm/jvms/instructions/loadStore/ldc2_w/ldc2_w05/ldc2_w0502/ldc2_w0502p/<init>()V
  aload_0
  invokevirtual org/apache/harmony/vts/test/vm/jvms/instructions/loadStore/ldc2_w/ldc2_w05/ldc2_w0502/ldc2_w0502p/test([Ljava/lang/String;)I
  invokestatic java/lang/System/exit(I)V

  return
.end method
