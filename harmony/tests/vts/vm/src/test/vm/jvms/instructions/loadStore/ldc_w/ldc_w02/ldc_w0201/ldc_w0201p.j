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

.class public org/apache/harmony/vts/test/vm/jvms/instructions/loadStore/ldc_w/ldc_w02/ldc_w0201/ldc_w0201p
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
ldc 1.0
pop
ldc 2.0
pop
ldc 3.0
pop
ldc 4.0
pop
ldc 5.0
pop
ldc 6.0
pop
ldc 7.0
pop
ldc 8.0
pop
ldc 9.0
pop
ldc 10.0
pop
ldc 11.0
pop
ldc 12.0
pop
ldc 13.0
pop
ldc 14.0
pop
ldc 15.0
pop
ldc 16.0
pop
ldc 17.0
pop
ldc 18.0
pop
ldc 19.0
pop
ldc 20.0
pop
ldc 21.0
pop
ldc 22.0
pop
ldc 23.0
pop
ldc 24.0
pop
ldc 25.0
pop
ldc 26.0
pop
ldc 27.0
pop
ldc 28.0
pop
ldc 29.0
pop
ldc 30.0
pop
ldc 31.0
pop
ldc 32.0
pop
ldc 33.0
pop
ldc 34.0
pop
ldc 35.0
pop
ldc 36.0
pop
ldc 37.0
pop
ldc 38.0
pop
ldc 39.0
pop
ldc 40.0
pop
ldc 41.0
pop
ldc 42.0
pop
ldc 43.0
pop
ldc 44.0
pop
ldc 45.0
pop
ldc 46.0
pop
ldc 47.0
pop
ldc 48.0
pop
ldc 49.0
pop
ldc 50.0
pop
ldc 51.0
pop
ldc 52.0
pop
ldc 53.0
pop
ldc 54.0
pop
ldc 55.0
pop
ldc 56.0
pop
ldc 57.0
pop
ldc 58.0
pop
ldc 59.0
pop
ldc 60.0
pop
ldc 61.0
pop
ldc 62.0
pop
ldc 63.0
pop
ldc 64.0
pop
ldc 65.0
pop
ldc 66.0
pop
ldc 67.0
pop
ldc 68.0
pop
ldc 69.0
pop
ldc 70.0
pop
ldc 71.0
pop
ldc 72.0
pop
ldc 73.0
pop
ldc 74.0
pop
ldc 75.0
pop
ldc 76.0
pop
ldc 77.0
pop
ldc 78.0
pop
ldc 79.0
pop
ldc 80.0
pop
ldc 81.0
pop
ldc 82.0
pop
ldc 83.0
pop
ldc 84.0
pop
ldc 85.0
pop
ldc 86.0
pop
ldc 87.0
pop
ldc 88.0
pop
ldc 89.0
pop
ldc 90.0
pop
ldc 91.0
pop
ldc 92.0
pop
ldc 93.0
pop
ldc 94.0
pop
ldc 95.0
pop
ldc 96.0
pop
ldc 97.0
pop
ldc 98.0
pop
ldc 99.0
pop
ldc 100.0
pop
ldc 101.0
pop
ldc 102.0
pop
ldc 103.0
pop
ldc 104.0
pop
ldc 105.0
pop
ldc 106.0
pop
ldc 107.0
pop
ldc 108.0
pop
ldc 109.0
pop
ldc 110.0
pop
ldc 111.0
pop
ldc 112.0
pop
ldc 113.0
pop
ldc 114.0
pop
ldc 115.0
pop
ldc 116.0
pop
ldc 117.0
pop
ldc 118.0
pop
ldc 119.0
pop
ldc 120.0
pop
ldc 121.0
pop
ldc 122.0
pop
ldc 123.0
pop
ldc 124.0
pop
ldc 125.0
pop
ldc 126.0
pop
ldc 127.0
pop
ldc 128.0
pop
ldc 129.0
pop
ldc 130.0
pop
ldc 131.0
pop
ldc 132.0
pop
ldc 133.0
pop
ldc 134.0
pop
ldc 135.0
pop
ldc 136.0
pop
ldc 137.0
pop
ldc 138.0
pop
ldc 139.0
pop
ldc 140.0
pop
ldc 141.0
pop
ldc 142.0
pop
ldc 143.0
pop
ldc 144.0
pop
ldc 145.0
pop
ldc 146.0
pop
ldc 147.0
pop
ldc 148.0
pop
ldc 149.0
pop
ldc 150.0
pop
ldc 151.0
pop
ldc 152.0
pop
ldc 153.0
pop
ldc 154.0
pop
ldc 155.0
pop
ldc 156.0
pop
ldc 157.0
pop
ldc 158.0
pop
ldc 159.0
pop
ldc 160.0
pop
ldc 161.0
pop
ldc 162.0
pop
ldc 163.0
pop
ldc 164.0
pop
ldc 165.0
pop
ldc 166.0
pop
ldc 167.0
pop
ldc 168.0
pop
ldc 169.0
pop
ldc 170.0
pop
ldc 171.0
pop
ldc 172.0
pop
ldc 173.0
pop
ldc 174.0
pop
ldc 175.0
pop
ldc 176.0
pop
ldc 177.0
pop
ldc 178.0
pop
ldc 179.0
pop
ldc 180.0
pop
ldc 181.0
pop
ldc 182.0
pop
ldc 183.0
pop
ldc 184.0
pop
ldc 185.0
pop
ldc 186.0
pop
ldc 187.0
pop
ldc 188.0
pop
ldc 189.0
pop
ldc 190.0
pop
ldc 191.0
pop
ldc 192.0
pop
ldc 193.0
pop
ldc 194.0
pop
ldc 195.0
pop
ldc 196.0
pop
ldc 197.0
pop
ldc 198.0
pop
ldc 199.0
pop
ldc 200.0
pop
ldc 201.0
pop
ldc 202.0
pop
ldc 203.0
pop
ldc 204.0
pop
ldc 205.0
pop
ldc 206.0
pop
ldc 207.0
pop
ldc 208.0
pop
ldc 209.0
pop
ldc 210.0
pop
ldc 211.0
pop
ldc 212.0
pop
ldc 213.0
pop
ldc 214.0
pop
ldc 215.0
pop
ldc 216.0
pop
ldc 217.0
pop
ldc 218.0
pop
ldc 219.0
pop
ldc 220.0
pop
ldc 221.0
pop
ldc 222.0
pop
ldc 223.0
pop
ldc 224.0
pop
ldc 225.0
pop
ldc 226.0
pop
ldc 227.0
pop
ldc 228.0
pop
ldc 229.0
pop
ldc 230.0
pop
ldc 231.0
pop
ldc 232.0
pop
ldc 233.0
pop
ldc 234.0
pop
ldc 235.0
pop
ldc 236.0
pop
ldc 237.0
pop
ldc 238.0
pop
ldc 239.0
pop
ldc 240.0
pop
ldc 241.0
pop
ldc 242.0
pop
ldc 243.0
pop
ldc 244.0
pop
ldc 245.0
pop
ldc 246.0
pop
ldc 247.0
pop
ldc 248.0
pop
ldc 249.0
pop
ldc 250.0
pop
ldc 251.0
pop
ldc 252.0
pop
ldc 253.0
pop
ldc 254.0
pop
ldc 255.0
pop
ldc 256.0
pop
ldc 257.0
pop
ldc 258.0
pop
ldc 259.0
pop
ldc 260.0
pop
ldc 261.0
pop
ldc 262.0
pop
ldc 263.0
pop
ldc 264.0
pop
ldc 265.0
pop
ldc 266.0
pop
ldc 267.0
pop
ldc 268.0
pop
ldc 269.0
pop
ldc 270.0
pop
ldc 271.0
pop
ldc 272.0
pop
ldc 273.0
pop
ldc 274.0
pop
ldc 275.0
pop
ldc 276.0
pop
ldc 277.0
pop
ldc 278.0
pop
ldc 279.0
pop
ldc 280.0
pop
ldc 281.0
pop
ldc 282.0
pop
ldc 283.0
pop
ldc 284.0
pop
ldc 285.0
pop
ldc 286.0
pop
ldc 287.0
pop
ldc 288.0
pop
ldc 289.0
pop
ldc 290.0
pop
ldc 291.0
pop
ldc 292.0
pop
ldc 293.0
pop
ldc 294.0
pop
ldc 295.0
pop
ldc 296.0
pop
ldc 297.0
pop
ldc 298.0
pop
ldc 299.0
pop
ldc 300.0
pop
ldc 301.0
pop
ldc 302.0
pop
ldc 303.0
pop
ldc 304.0
pop
ldc 305.0
pop
ldc 306.0
pop
ldc 307.0
pop
ldc 308.0
pop
ldc 309.0
pop
ldc 310.0
pop
ldc 311.0
pop
ldc 312.0
pop
ldc 313.0
pop
ldc 314.0
pop
ldc 315.0
pop
ldc 316.0
pop
ldc 317.0
pop
ldc 318.0
pop
ldc 319.0
pop
ldc 320.0
pop
ldc 321.0
pop
ldc 322.0
pop
ldc 323.0
pop
ldc 324.0
pop
ldc 325.0
pop
ldc 326.0
pop
ldc 327.0
pop
ldc 328.0
pop
ldc 329.0
pop
ldc 330.0
pop
ldc 331.0
pop
ldc 332.0
pop
ldc 333.0
pop
ldc 334.0
pop
ldc 335.0
pop
ldc 336.0
pop
ldc 337.0
pop
ldc 338.0
pop
ldc 339.0
pop
ldc 340.0
pop
ldc 341.0
pop
ldc 342.0
pop
ldc 343.0
pop
ldc 344.0
pop
ldc 345.0
pop
ldc 346.0
pop
ldc 347.0
pop
ldc 348.0
pop
ldc 349.0
pop
ldc 350.0
pop
ldc 351.0
pop
ldc 352.0
pop
ldc 353.0
pop
ldc 354.0
pop
ldc 355.0
pop
ldc 356.0
pop
ldc 357.0
pop
ldc 358.0
pop
ldc 359.0
pop
ldc 360.0
pop
ldc 361.0
pop
ldc 362.0
pop
ldc 363.0
pop
ldc 364.0
pop
ldc 365.0
pop
ldc 366.0
pop
ldc 367.0
pop
ldc 368.0
pop
ldc 369.0
pop
ldc 370.0
pop
ldc 371.0
pop
ldc 372.0
pop
ldc 373.0
pop
ldc 374.0
pop
ldc 375.0
pop
ldc 376.0
pop
ldc 377.0
pop
ldc 378.0
pop
ldc 379.0
pop
ldc 380.0
pop
ldc 381.0
pop
ldc 382.0
pop
ldc 383.0
pop
ldc 384.0
pop
ldc 385.0
pop
ldc 386.0
pop
ldc 387.0
pop
ldc 388.0
pop
ldc 389.0
pop
ldc 390.0
pop
ldc 391.0
pop
ldc 392.0
pop
ldc 393.0
pop
ldc 394.0
pop
ldc 395.0
pop
ldc 396.0
pop
ldc 397.0
pop
ldc 398.0
pop
ldc 399.0
pop
ldc 400.0
pop
ldc 401.0
pop
ldc 402.0
pop
ldc 403.0
pop
ldc 404.0
pop
ldc 405.0
pop
ldc 406.0
pop
ldc 407.0
pop
ldc 408.0
pop
ldc 409.0
pop
ldc 410.0
pop
ldc 411.0
pop
ldc 412.0
pop
ldc 413.0
pop
ldc 414.0
pop
ldc 415.0
pop
ldc 416.0
pop
ldc 417.0
pop
ldc 418.0
pop
ldc 419.0
pop
ldc 420.0
pop
ldc 421.0
pop
ldc 422.0
pop
ldc 423.0
pop
ldc 424.0
pop
ldc 425.0
pop
ldc 426.0
pop
ldc 427.0
pop
ldc 428.0
pop
ldc 429.0
pop
ldc 430.0
pop
ldc 431.0
pop
ldc 432.0
pop
ldc 433.0
pop
ldc 434.0
pop
ldc 435.0
pop
ldc 436.0
pop
ldc 437.0
pop
ldc 438.0
pop
ldc 439.0
pop
ldc 440.0
pop
ldc 441.0
pop
ldc 442.0
pop
ldc 443.0
pop
ldc 444.0
pop
ldc 445.0
pop
ldc 446.0
pop
ldc 447.0
pop
ldc 448.0
pop
ldc 449.0
pop
ldc 450.0
pop
ldc 451.0
pop
ldc 452.0
pop
ldc 453.0
pop
ldc 454.0
pop
ldc 455.0
pop
ldc 456.0
pop
ldc 457.0
pop
ldc 458.0
pop
ldc 459.0
pop
ldc 460.0
pop
ldc 461.0
pop
ldc 462.0
pop
ldc 463.0
pop
ldc 464.0
pop
ldc 465.0
pop
ldc 466.0
pop
ldc 467.0
pop
ldc 468.0
pop
ldc 469.0
pop
ldc 470.0
pop
ldc 471.0
pop
ldc 472.0
pop
ldc 473.0
pop
ldc 474.0
pop
ldc 475.0
pop
ldc 476.0
pop
ldc 477.0
pop
ldc 478.0
pop
ldc 479.0
pop
ldc 480.0
pop
ldc 481.0
pop
ldc 482.0
pop
ldc 483.0
pop
ldc 484.0
pop
ldc 485.0
pop
ldc 486.0
pop
ldc 487.0
pop
ldc 488.0
pop
ldc 489.0
pop
ldc 490.0
pop
ldc 491.0
pop
ldc 492.0
pop
ldc 493.0
pop
ldc 494.0
pop
ldc 495.0
pop
ldc 496.0
pop
ldc 497.0
pop
ldc 498.0
pop
ldc 499.0
pop
ldc 500.0
pop
ldc 501.0
pop
ldc 502.0
pop
ldc 503.0
pop
ldc 504.0
pop
ldc 505.0
pop
ldc 506.0
pop
ldc 507.0
pop
ldc 508.0
pop
ldc 509.0
pop
ldc 510.0
pop
ldc 511.0
pop
ldc 512.0
pop
ldc 513.0
pop
ldc 514.0
pop
ldc 515.0
pop
ldc 516.0
pop
ldc 517.0
pop
ldc 518.0
pop
ldc 519.0
pop
ldc 520.0
pop
ldc 521.0
pop
ldc 522.0
pop
ldc 523.0
pop
ldc 524.0
pop
ldc 525.0
pop
ldc 526.0
pop
ldc 527.0
pop
ldc 528.0
pop
ldc 529.0
pop
ldc 530.0
pop
ldc 531.0
pop
ldc 532.0
pop
ldc 533.0
pop
ldc 534.0
pop
ldc 535.0
pop
ldc 536.0
pop
ldc 537.0
pop
ldc 538.0
pop
ldc 539.0
pop
ldc 540.0
pop
ldc 541.0
pop
ldc 542.0
pop
ldc 543.0
pop
ldc 544.0
pop
ldc 545.0
pop
ldc 546.0
pop
ldc 547.0
pop
ldc 548.0
pop
ldc 549.0
pop
ldc 550.0
pop
ldc 551.0
pop
ldc 552.0
pop
ldc 553.0
pop
ldc 554.0
pop
ldc 555.0
pop
ldc 556.0
pop
ldc 557.0
pop
ldc 558.0
pop
ldc 559.0
pop
ldc 560.0
pop
ldc 561.0
pop
ldc 562.0
pop
ldc 563.0
pop
ldc 564.0
pop
ldc 565.0
pop
ldc 566.0
pop
ldc 567.0
pop
ldc 568.0
pop
ldc 569.0
pop
ldc 570.0
pop
ldc 571.0
pop
ldc 572.0
pop
ldc 573.0
pop
ldc 574.0
pop
ldc 575.0
pop
ldc 576.0
pop
ldc 577.0
pop
ldc 578.0
pop
ldc 579.0
pop
ldc 580.0
pop
ldc 581.0
pop
ldc 582.0
pop
ldc 583.0
pop
ldc 584.0
pop
ldc 585.0
pop
ldc 586.0
pop
ldc 587.0
pop
ldc 588.0
pop
ldc 589.0
pop
ldc 590.0
pop
ldc 591.0
pop
ldc 592.0
pop
ldc 593.0
pop
ldc 594.0
pop
ldc 595.0
pop
ldc 596.0
pop
ldc 597.0
pop
ldc 598.0
pop
ldc 599.0
pop
ldc 600.0
pop
ldc 601.0
pop
ldc 602.0
pop
ldc 603.0
pop
ldc 604.0
pop
ldc 605.0
pop
ldc 606.0
pop
ldc 607.0
pop
ldc 608.0
pop
ldc 609.0
pop
ldc 610.0
pop
ldc 611.0
pop
ldc 612.0
pop
ldc 613.0
pop
ldc 614.0
pop
ldc 615.0
pop
ldc 616.0
pop
ldc 617.0
pop
ldc 618.0
pop
ldc 619.0
pop
ldc 620.0
pop
ldc 621.0
pop
ldc 622.0
pop
ldc 623.0
pop
ldc 624.0
pop
ldc 625.0
pop
ldc 626.0
pop
ldc 627.0
pop
ldc 628.0
pop
ldc 629.0
pop
ldc 630.0
pop
ldc 631.0
pop
ldc 632.0
pop
ldc 633.0
pop
ldc 634.0
pop
ldc 635.0
pop
ldc 636.0
pop
ldc 637.0
pop
ldc 638.0
pop
ldc 639.0
pop
ldc 640.0
pop
ldc 641.0
pop
ldc 642.0
pop
ldc 643.0
pop
ldc 644.0
pop
ldc 645.0
pop
ldc 646.0
pop
ldc 647.0
pop
ldc 648.0
pop
ldc 649.0
pop
ldc 650.0
pop
ldc 651.0
pop
ldc 652.0
pop
ldc 653.0
pop
ldc 654.0
pop
ldc 655.0
pop
ldc 656.0
pop
ldc 657.0
pop
ldc 658.0
pop
ldc 659.0
pop
ldc 660.0
pop
ldc 661.0
pop
ldc 662.0
pop
ldc 663.0
pop
ldc 664.0
pop
ldc 665.0
pop
ldc 666.0
pop
ldc 667.0
pop
ldc 668.0
pop
ldc 669.0
pop
ldc 670.0
pop
ldc 671.0
pop
ldc 672.0
pop
ldc 673.0
pop
ldc 674.0
pop
ldc 675.0
pop
ldc 676.0
pop
ldc 677.0
pop
ldc 678.0
pop
ldc 679.0
pop
ldc 680.0
pop
ldc 681.0
pop
ldc 682.0
pop
ldc 683.0
pop
ldc 684.0
pop
ldc 685.0
pop
ldc 686.0
pop
ldc 687.0
pop
ldc 688.0
pop
ldc 689.0
pop
ldc 690.0
pop
ldc 691.0
pop
ldc 692.0
pop
ldc 693.0
pop
ldc 694.0
pop
ldc 695.0
pop
ldc 696.0
pop
ldc 697.0
pop
ldc 698.0
pop
ldc 699.0
pop
ldc 700.0
pop
ldc 701.0
pop
ldc 702.0
pop
ldc 703.0
pop
ldc 704.0
pop
ldc 705.0
pop
ldc 706.0
pop
ldc 707.0
pop
ldc 708.0
pop
ldc 709.0
pop
ldc 710.0
pop
ldc 711.0
pop
ldc 712.0
pop
ldc 713.0
pop
ldc 714.0
pop
ldc 715.0
pop
ldc 716.0
pop
ldc 717.0
pop
ldc 718.0
pop
ldc 719.0
pop
ldc 720.0
pop
ldc 721.0
pop
ldc 722.0
pop
ldc 723.0
pop
ldc 724.0
pop
ldc 725.0
pop
ldc 726.0
pop
ldc 727.0
pop
ldc 728.0
pop
ldc 729.0
pop
ldc 730.0
pop
ldc 731.0
pop
ldc 732.0
pop
ldc 733.0
pop
ldc 734.0
pop
ldc 735.0
pop
ldc 736.0
pop
ldc 737.0
pop
ldc 738.0
pop
ldc 739.0
pop
ldc 740.0
pop
ldc 741.0
pop
ldc 742.0
pop
ldc 743.0
pop
ldc 744.0
pop
ldc 745.0
pop
ldc 746.0
pop
ldc 747.0
pop
ldc 748.0
pop
ldc 749.0
pop
ldc 750.0
pop
ldc 751.0
pop
ldc 752.0
pop
ldc 753.0
pop
ldc 754.0
pop
ldc 755.0
pop
ldc 756.0
pop
ldc 757.0
pop
ldc 758.0
pop
ldc 759.0
pop
ldc 760.0
pop
ldc 761.0
pop
ldc 762.0
pop
ldc 763.0
pop
ldc 764.0
pop
ldc 765.0
pop
ldc 766.0
pop
ldc 767.0
pop
ldc 768.0
pop
ldc 769.0
pop
ldc 770.0
pop
ldc 771.0
pop
ldc 772.0
pop
ldc 773.0
pop
ldc 774.0
pop
ldc 775.0
pop
ldc 776.0
pop
ldc 777.0
pop
ldc 778.0
pop
ldc 779.0
pop
ldc 780.0
pop
ldc 781.0
pop
ldc 782.0
pop
ldc 783.0
pop
ldc 784.0
pop
ldc 785.0
pop
ldc 786.0
pop
ldc 787.0
pop
ldc 788.0
pop
ldc 789.0
pop
ldc 790.0
pop
ldc 791.0
pop
ldc 792.0
pop
ldc 793.0
pop
ldc 794.0
pop
ldc 795.0
pop
ldc 796.0
pop
ldc 797.0
pop
ldc 798.0
pop
ldc 799.0
pop
ldc 800.0
pop
ldc 801.0
pop
ldc 802.0
pop
ldc 803.0
pop
ldc 804.0
pop
ldc 805.0
pop
ldc 806.0
pop
ldc 807.0
pop
ldc 808.0
pop
ldc 809.0
pop
ldc 810.0
pop
ldc 811.0
pop
ldc 812.0
pop
ldc 813.0
pop
ldc 814.0
pop
ldc 815.0
pop
ldc 816.0
pop
ldc 817.0
pop
ldc 818.0
pop
ldc 819.0
pop
ldc 820.0
pop
ldc 821.0
pop
ldc 822.0
pop
ldc 823.0
pop
ldc 824.0
pop
ldc 825.0
pop
ldc 826.0
pop
ldc 827.0
pop
ldc 828.0
pop
ldc 829.0
pop
ldc 830.0
pop
ldc 831.0
pop
ldc 832.0
pop
ldc 833.0
pop
ldc 834.0
pop
ldc 835.0
pop
ldc 836.0
pop
ldc 837.0
pop
ldc 838.0
pop
ldc 839.0
pop
ldc 840.0
pop
ldc 841.0
pop
ldc 842.0
pop
ldc 843.0
pop
ldc 844.0
pop
ldc 845.0
pop
ldc 846.0
pop
ldc 847.0
pop
ldc 848.0
pop
ldc 849.0
pop
ldc 850.0
pop
ldc 851.0
pop
ldc 852.0
pop
ldc 853.0
pop
ldc 854.0
pop
ldc 855.0
pop
ldc 856.0
pop
ldc 857.0
pop
ldc 858.0
pop
ldc 859.0
pop
ldc 860.0
pop
ldc 861.0
pop
ldc 862.0
pop
ldc 863.0
pop
ldc 864.0
pop
ldc 865.0
pop
ldc 866.0
pop
ldc 867.0
pop
ldc 868.0
pop
ldc 869.0
pop
ldc 870.0
pop
ldc 871.0
pop
ldc 872.0
pop
ldc 873.0
pop
ldc 874.0
pop
ldc 875.0
pop
ldc 876.0
pop
ldc 877.0
pop
ldc 878.0
pop
ldc 879.0
pop
ldc 880.0
pop
ldc 881.0
pop
ldc 882.0
pop
ldc 883.0
pop
ldc 884.0
pop
ldc 885.0
pop
ldc 886.0
pop
ldc 887.0
pop
ldc 888.0
pop
ldc 889.0
pop
ldc 890.0
pop
ldc 891.0
pop
ldc 892.0
pop
ldc 893.0
pop
ldc 894.0
pop
ldc 895.0
pop
ldc 896.0
pop
ldc 897.0
pop
ldc 898.0
pop
ldc 899.0
pop
ldc 900.0
pop
ldc 901.0
pop
ldc 902.0
pop
ldc 903.0
pop
ldc 904.0
pop
ldc 905.0
pop
ldc 906.0
pop
ldc 907.0
pop
ldc 908.0
pop
ldc 909.0
pop
ldc 910.0
pop
ldc 911.0
pop
ldc 912.0
pop
ldc 913.0
pop
ldc 914.0
pop
ldc 915.0
pop
ldc 916.0
pop
ldc 917.0
pop
ldc 918.0
pop
ldc 919.0
pop
ldc 920.0
pop
ldc 921.0
pop
ldc 922.0
pop
ldc 923.0
pop
ldc 924.0
pop
ldc 925.0
pop
ldc 926.0
pop
ldc 927.0
pop
ldc 928.0
pop
ldc 929.0
pop
ldc 930.0
pop
ldc 931.0
pop
ldc 932.0
pop
ldc 933.0
pop
ldc 934.0
pop
ldc 935.0
pop
ldc 936.0
pop
ldc 937.0
pop
ldc 938.0
pop
ldc 939.0
pop
ldc 940.0
pop
ldc 941.0
pop
ldc 942.0
pop
ldc 943.0
pop
ldc 944.0
pop
ldc 945.0
pop
ldc 946.0
pop
ldc 947.0
pop
ldc 948.0
pop
ldc 949.0
pop
ldc 950.0
pop
ldc 951.0
pop
ldc 952.0
pop
ldc 953.0
pop
ldc 954.0
pop
ldc 955.0
pop
ldc 956.0
pop
ldc 957.0
pop
ldc 958.0
pop
ldc 959.0
pop
ldc 960.0
pop
ldc 961.0
pop
ldc 962.0
pop
ldc 963.0
pop
ldc 964.0
pop
ldc 965.0
pop
ldc 966.0
pop
ldc 967.0
pop
ldc 968.0
pop
ldc 969.0
pop
ldc 970.0
pop
ldc 971.0
pop
ldc 972.0
pop
ldc 973.0
pop
ldc 974.0
pop
ldc 975.0
pop
ldc 976.0
pop
ldc 977.0
pop
ldc 978.0
pop
ldc 979.0
pop
ldc 980.0
pop
ldc 981.0
pop
ldc 982.0
pop
ldc 983.0
pop
ldc 984.0
pop
ldc 985.0
pop
ldc 986.0
pop
ldc 987.0
pop
ldc 988.0
pop
ldc 989.0
pop
ldc 990.0
pop
ldc 991.0
pop
ldc 992.0
pop
ldc 993.0
pop
ldc 994.0
pop
ldc 995.0
pop
ldc 996.0
pop
ldc 997.0
pop
ldc 998.0
pop
ldc 999.0
pop
;----------------------------------------
   ldc_w 555.5 ; must push float 2
   ldc_w 555.5 ; push float 2
   fcmpl
   ifne Fail ; if value != 2
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

  new org/apache/harmony/vts/test/vm/jvms/instructions/loadStore/ldc_w/ldc_w02/ldc_w0201/ldc_w0201p
  dup
  invokespecial org/apache/harmony/vts/test/vm/jvms/instructions/loadStore/ldc_w/ldc_w02/ldc_w0201/ldc_w0201p/<init>()V
  aload_0
  invokevirtual org/apache/harmony/vts/test/vm/jvms/instructions/loadStore/ldc_w/ldc_w02/ldc_w0201/ldc_w0201p/test([Ljava/lang/String;)I
  invokestatic java/lang/System/exit(I)V

  return
.end method
