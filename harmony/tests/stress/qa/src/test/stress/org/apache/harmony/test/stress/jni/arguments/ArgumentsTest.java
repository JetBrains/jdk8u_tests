/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */    

/**
 * @author Vladimir Nenashev
 * @version $Revision: 1.6 $
 */

package org.apache.harmony.test.stress.jni.arguments;

import junit.framework.TestCase;

import org.apache.harmony.test.stress.jni.share.JNITestError;
import org.apache.harmony.test.share.stress.ReliabilityRunner;

public class ArgumentsTest extends TestCase {

    private native boolean nativeMethod(char param0, Object param1,
            boolean param2, double param3, int param4, double param5,
            long param6, boolean param7, boolean param8, int param9,
            Object param10, short param11, Object param12, float param13,
            char param14, Object param15, long param16, double param17,
            boolean param18, Object param19, double param20, float param21,
            char param22, boolean param23, float param24, float param25,
            boolean param26, int param27, int param28, double param29,
            float param30, int param31, int param32, double param33,
            int param34, int param35, long param36, int param37,
            boolean param38, float param39, Object param40, int param41,
            double param42, double param43, double param44, double param45,
            Object param46, Object param47, short param48, float param49,
            long param50, double param51, boolean param52, float param53,
            char param54, Object param55, float param56, long param57,
            short param58, float param59, long param60, char param61,
            boolean param62, float param63, char param64, int param65,
            double param66, float param67, Object param68, char param69,
            short param70, double param71, long param72, Object param73,
            double param74, double param75, float param76, float param77,
            char param78, boolean param79, Object param80, long param81,
            int param82, float param83, long param84, int param85,
            float param86, short param87, double param88, char param89,
            char param90, int param91, double param92, float param93,
            char param94, long param95, long param96, boolean param97,
            char param98, boolean param99, long param100, double param101,
            int param102, double param103, double param104, long param105,
            char param106, short param107, Object param108, short param109,
            Object param110, int param111, float param112, Object param113,
            float param114, float param115, boolean param116, Object param117,
            Object param118, Object param119, double param120, short param121,
            Object param122, Object param123, Object param124, char param125,
            long param126, short param127, boolean param128, short param129,
            boolean param130, Object param131, Object param132,
            double param133, short param134, short param135, boolean param136,
            long param137, int param138, float param139, long param140,
            double param141, char param142, float param143, Object param144,
            long param145, short param146, int param147, int param148,
            Object param149, boolean param150, Object param151, int param152,
            char param153, long param154, Object param155, char param156,
            boolean param157, float param158);

    public void test() {
        boolean res = nativeMethod('y', this, false, 0.6880734650254612,
                -459389370, 0.735613490345237, -766507610760164852l, true,
                true, -822206411, this, (short) -11723, this, 0.14847177f, 'c',
                this, 9220033352967053857l, 0.3096336427628982, false, this,
                0.45558886336638693, 0.49166316f, 'f', false, 0.38152766f,
                0.22172415f, false, -1256161581, -1099685115,
                0.7775119661901024, 0.9402661f, 1355398521, -103698453,
                0.4130428904299823, -1146908240, 931763038,
                -4786854144841381960l, 1954868242, true, 0.08739269f, this,
                -1982838329, 0.14883036934542238, 0.7423147936947179,
                0.5103701463478881, 0.1685154438854033, this, this,
                (short) -20496, 0.73771083f, -5327611971083507949l,
                0.9131962395535225, false, 0.47935134f, 'k', this, 0.67694134f,
                3306191666340008843l, (short) 26949, 0.40150875f,
                -4756300552179881079l, 'o', false, 0.28298575f, 'a',
                -1074626584, 0.21357118229536398, 0.63859355f, this, 'w',
                (short) -26532, 0.5670683499696793, -4151965278356844424l,
                this, 0.49673613390135296, 0.7997111898824626, 0.097144604f,
                0.68615514f, 'd', false, this, 7994147225335491508l,
                1962416039, 0.17836106f, -6509010608434418352l, 173972112,
                0.6935554f, (short) 17, 0.04382217780782216, 'v', 'k',
                1188430022, 0.8278033679671325, 0.2787966f, 'w',
                4456215152153757254l, 5024596775803882053l, true, 'a', true,
                3456444896829275243l, 0.30075119346177526, -1435747473,
                0.05459499927950484, 0.8379766626155045, -6564361361229661956l,
                'o', (short) -29240, this, (short) 4931, this, 1185833733,
                0.71725607f, this, 0.994242f, 0.3262784f, false, this, this,
                this, 0.9035337365976047, (short) 18120, this, this, this, 'q',
                -3520397967523792049l, (short) 30554, true, (short) -22592,
                true, this, this, 0.5330963426115407, (short) 21978,
                (short) 16745, true, -5860487575907610989l, -937905170,
                0.03567344f, -8033189936759820605l, 0.8580090537111951, 'o',
                0.24530011f, this, 3937150454368020805l, (short) 27042,
                308498473, 769126397, this, false, this, -2088622482, 'k',
                2600599697325177763l, this, 'f', false, 0.02885884f);
        if (!res) {
            ReliabilityRunner.debug("Test failed");
            ReliabilityRunner.mainTest.addError(this, new JNITestError());
        }
    }

    static {
        System.loadLibrary("jnitests");
    }

}
