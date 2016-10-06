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

.class public org/apache/harmony/vts/test/vm/jvms/instructions/loadStore/ldc_w/ldc_w01/ldc_w0101/ldc_w0101p
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
; -------------------------------------------------
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
;--------------------------------------------------

   ldc_w 555 ; must push 555
   sipush 555 ; push 555
   if_icmpne Fail ; if value != 555
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

  new org/apache/harmony/vts/test/vm/jvms/instructions/loadStore/ldc_w/ldc_w01/ldc_w0101/ldc_w0101p
  dup
  invokespecial org/apache/harmony/vts/test/vm/jvms/instructions/loadStore/ldc_w/ldc_w01/ldc_w0101/ldc_w0101p/<init>()V
  aload_0
  invokevirtual org/apache/harmony/vts/test/vm/jvms/instructions/loadStore/ldc_w/ldc_w01/ldc_w0101/ldc_w0101p/test([Ljava/lang/String;)I
  invokestatic java/lang/System/exit(I)V

  return
.end method
