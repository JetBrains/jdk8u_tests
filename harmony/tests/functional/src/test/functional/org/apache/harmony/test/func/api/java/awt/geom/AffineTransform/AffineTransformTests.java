/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
/**
 */

package org.apache.harmony.test.func.api.java.awt.geom.AffineTransform;

import org.apache.harmony.share.*;

import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;

public class AffineTransformTests extends Test {

    private int testStatus;
    private double eps = 1e-8;

    AffineTransformTests() {
        super();
        testStatus = super.pass("");
    };

    public int test() {
        double dx = Math.rint(1.0 + Math.random() * 100);
        double dy = Math.rint(1.0 + Math.random() * 100);
        float fdx = (float) Math.rint(1.0 + Math.random() * 100);
        float fdy = (float) Math.rint(1.0 + Math.random() * 100);

        log.info("dx is: \t" + dx + "\t dy is: \t" + dy);
        log.info("fdx is: \t" + fdx + "\t fdy is: \t" + fdy);
//-------------------------------
        AffineTransform tx = new AffineTransform();
        AffineTransform tx_tmp = new AffineTransform();
        AffineTransform rotate_quadr_tx = AffineTransform.getRotateInstance(Math.PI / 2.0);
        AffineTransform rotate_2quadr_tx = new AffineTransform(AffineTransform.getRotateInstance(Math.PI));

        assert_transform_type_correct(rotate_quadr_tx, AffineTransform.TYPE_QUADRANT_ROTATION, "first");

//-------------------------------
        AffineTransform translate_tx = AffineTransform.getTranslateInstance(dx, dy);
        AffineTransform translate_tx_back = null;
        try {
            translate_tx_back = translate_tx.createInverse();
        } catch (NoninvertibleTransformException e) {
            if (0.0 != translate_tx.getDeterminant()) {
                testStatus = fail(e.toString() + "\n" +
                        " non zero transform " + translate_tx + " didn't inverts \n" +
                        " det[ " + translate_tx + " ] is:\t" + translate_tx.getDeterminant());
            } else {
                e.printStackTrace();
            }
            translate_tx.setTransform(AffineTransform.getTranslateInstance(-dx, -dy));
        }
        assert_transform_type_correct(translate_tx, AffineTransform.TYPE_TRANSLATION, "second");
//-------------------------------
        if (dx != translate_tx.getTranslateX()) {
            testStatus = fail(" dx != translate_tx.getTranslateX() ");
        }
        if (dy != translate_tx.getTranslateY()) {
            testStatus = fail(" dy != translate_tx.getTranslateY() ");
        }

        //---------
        // tx.concatenate(rotate_2quadr_tx);
        tx.setToRotation(Math.PI / 2.0);
        tx.concatenate(translate_tx);
        assert_transform_type_correct(tx, AffineTransform.TYPE_TRANSLATION +
                AffineTransform.TYPE_QUADRANT_ROTATION, "third");
        //---------
        //tx.setToIdentity();
        tx.setTransform(rotate_2quadr_tx);
        tx.concatenate(rotate_quadr_tx);
        tx.rotate(Math.PI / 2.0);
        assert_transform_type_correct(tx, AffineTransform.TYPE_IDENTITY, "fourth");

        //---------
        //tx.setToIdentity();
        tx.setTransform(translate_tx);
        tx.concatenate(rotate_2quadr_tx);
        tx.concatenate(rotate_quadr_tx);
        tx.concatenate(rotate_quadr_tx);
        tx.concatenate(translate_tx_back);
        assert_transform_type_correct(tx, AffineTransform.TYPE_IDENTITY, "fifth");

//-------------------------------
        AffineTransform scale_tx = AffineTransform.getScaleInstance(dx, dy);
        double[] flatmatrix = {1.0 / dx, 0.0, 0.0, 1. / dy};
        AffineTransform scale_tx_back = new AffineTransform(flatmatrix);
        AffineTransform scale_tx_back_1 = AffineTransform.getScaleInstance(1 / dx, 1 / dy);
        //---------
        if (!scale_tx_back.equals(scale_tx_back_1)) {
            testStatus = fail(" ! scale_tx_back.equals(scale_tx_back_1) ");
        }
        //---------
        //tx.setToIdentity();
        tx.setTransform(scale_tx);
        tx.concatenate(scale_tx_back);
        assert_transform_type_correct(tx, AffineTransform.TYPE_IDENTITY, "sixth");

//-------------------------------
        AffineTransform scale_tx_2 = new AffineTransform(dx, 0.0, 0.0, dy, 0.0, 0.0);
        AffineTransform scale_tx_2_back = AffineTransform.getScaleInstance(1 / dx, 1 / dy);

        if (dx != scale_tx_2.getScaleX()) {
            testStatus = fail(" dx != scale_tx_2.getScaleX() ");
        }
        if (dy != scale_tx_2.getScaleY()) {
            testStatus = fail(" dy != scale_tx_2.getScaleY() ");
        }
        //---------

        tx.setToIdentity();
        tx.concatenate(scale_tx_2);
        tx.concatenate(scale_tx_2_back);
        assert_transform_type_correct(tx, AffineTransform.TYPE_IDENTITY, "seventh");

        //---------
        double[] matrix = new double[4];
        double det;
        double det2;
        scale_tx_2_back.getMatrix(matrix);
        det = scale_tx_2_back.getDeterminant();
        det2 = matrix[0] * matrix[3] - matrix[1] * matrix[2];

        if (det2 != det) {
            testStatus = fail(" det2 != det \n" +
                    matrix[0] + " \t" + matrix[1] + " \t" +
                    matrix[2] + " \t" + matrix[3] + " \t\n" +
                    Double.toString(matrix[0] * matrix[3] - matrix[1] * matrix[2]) + "\n" +
                    Double.toString(det));
        }

//-------------------------------
        AffineTransform scale_tx_3 = new AffineTransform((1.0 / fdx), 0.0, 0.0, (1.0 / fdy), 0.0, 0.0);
        float[] flatmatrix_fl = {fdx, 0, 0, fdy, 0, 0};
        AffineTransform scale_tx_3_back = new AffineTransform(flatmatrix_fl);
        //AffineTransform.getScaleInstance( fdx , fdy );

        tx.setToIdentity();
        tx.preConcatenate(scale_tx_3);
        tx.preConcatenate(scale_tx_3_back);
        assert_transform_type_correct(tx, AffineTransform.TYPE_IDENTITY, "eighth");

//-------------------------------
        AffineTransform shear_tx = AffineTransform.getShearInstance(dx, dy);
        AffineTransform shear_tx_back = AffineTransform.getShearInstance(1.0 / dx, 1.0 / dy);
        if (dx != shear_tx.getShearX()) {
            testStatus = fail(" dx != shear_tx_2.getShearX() ");
        }
        if (dy != shear_tx.getShearY()) {
            testStatus = fail(" dy != shear_tx_2.getShearY() ");
        }
        tx.setToIdentity();
        tx.shear(dx, dy);
        tx.preConcatenate(shear_tx_back);
        assert_transform_type_correct(tx, AffineTransform.TYPE_GENERAL_TRANSFORM, "nineth");

//-------------------------------
        Point2D pt_src = shear_tx.deltaTransform(new Point2D.Double(dx, dy), null);
        Point2D pt_dst = new Point2D.Double();
        Point2D pt_res = new Point2D.Double();

        pt_res = scale_tx.deltaTransform(pt_src, pt_dst);
        if (!pt_res.equals(pt_dst)) {
            testStatus = fail(" ! pt_res.equals(pt_dst) \n" +
                    " pt_src is: \t" + pt_src.toString() + "\n" +
                    " pt_dst is: \t" + pt_dst.toString() + "\n" +
                    " pt_res is: \t" + pt_res.toString());
        }

        try {
            pt_dst = scale_tx.inverseTransform(pt_res, null);
        } catch (NoninvertibleTransformException e) {
            if (0.0 != scale_tx.getDeterminant()) {
                testStatus = fail(e.toString() + "\n" +
                        " non zero transform " + scale_tx + " didn't invers \n" +
                        " det[ " + scale_tx + " ] is:\t" + scale_tx.getDeterminant());
            } else {
                e.printStackTrace();
            }
            pt_dst = scale_tx_back.deltaTransform(pt_res, null);

        }

        //
        if (!pt_dst.equals(pt_src)) {
            testStatus = fail(" ! pt_dst.equals(pt_src) \n" +
                    " pt_src is: \t" + pt_src.toString() + "\n" +
                    " pt_dst is: \t" + pt_dst.toString() + "\n" +
                    " pt_res is: \t" + pt_res.toString());
        }
//-------------------------------

        AffineTransform tx_rotate = AffineTransform.getRotateInstance(Math.PI / 2, dx, dy);

        tx.setToRotation(-Math.PI / 2, dx, dy);
        tx.concatenate(tx_rotate);
        if (!tx.isIdentity()) {
            testStatus = fail(" ! tx.isIdentity() \n" +
                    "tx is \t" + tx + "\n" +
                    "tx_rotate is \t" + tx_rotate);
        }

//-------------------------------
        AffineTransform tx_rotate_2 = (AffineTransform) tx_rotate.clone();

        pt_dst = tx_rotate_2.transform(pt_src, null);
        if (!pt_dst.equals(tx_rotate_2.transform(pt_src, null))) {
            testStatus = fail(" ! pt_dst.equals( tx_rotate_2.transform(pt_src, null) \n" +
                    "tx_rotate is \t" + tx_rotate + "\n" +
                    "tx_rotate_2 is \t" + tx_rotate_2);
        }


//-------------------------------
        AffineTransform tx_2 = new AffineTransform();

        tx.setToTranslation(dx * dy, fdx * fdy);
        tx_2.setTransform(1.0, 0.0, 0.0, 1.0, dx * dy, fdx * fdy);

        if (!tx_2.equals(tx)) {
            testStatus = fail(" ! tx_2.equals(tx ) ");
            log.info("tx is \t" + tx);
            log.info("tx_2 is \t" + tx_2);
        }

//-------------------------------
        AffineTransform tx_3 = new AffineTransform();
        AffineTransform tx_4 = (AffineTransform) tx.clone();
        tx_3.translate(dx * dy, fdx * fdy);
        tx_4.setTransform(1.0, 0.0, 0.0, 1.0, dx * dy, fdx * fdy);
        ;

        if (tx_3.hashCode() != tx_4.hashCode()) {
            testStatus = fail(" tx_3.hashCode() !=  tx_4.hashCode() \n" +
                    "tx_3 is \t" + tx_3 + "\n" +
                    "tx_3.hashCode() is \t" + tx_3.hashCode() + "\n" +
                    "tx_4 is \t" + tx_4 + "\n" +
                    "tx_4.hashCode() is \t" + tx_4.hashCode());
        }
//-------------------------------

        return testStatus;
    }

    public static void main(String[] args) {
        System.exit(new AffineTransformTests().test(args));
    }


    private void assert_transform_type_correct(AffineTransform source, int comparableTtype, String text) {
        AffineTransform tx = new AffineTransform();
        if (comparableTtype == AffineTransform.TYPE_IDENTITY) {

            double[] matrixE = {1.0, 0.0, 0.0, 1.0, 0.0, 0.0};
            double[] matrix = new double[6];
            boolean isTrue = true;
            source.getMatrix(matrix);
            for (int i = 0; i < 6; ++i) {
                if (Math.abs(matrixE[i] - matrix[i]) > eps) {
                    isTrue = false;
                }
            }
            if (!isTrue) {
                tx.setTransform(source);
            }
        } else {
            tx.setTransform(source);
        }

        int sourceType = tx.getType();
        if (!compare_type(sourceType, comparableTtype)) {
            testStatus = fail("Error assertion \t" + text + "\n" +
                    "\tTest failed because getType() return unexpected type of AffineTransform\n" +
                    "\tAffineTransform is: \t" + source.toString() + "\n" +
                    "\tType of this AffineTransform is: \t" + sourceType + "\n" +
                    "\tType passed to compare (tranformTtype) is: \t" + comparableTtype);
        }
    }


    private boolean compare_type(int source, int template) {
        if (template == 0) {
            return (source == 0);
        } else {
            return ((source & template) == template);
        }
    }
}





