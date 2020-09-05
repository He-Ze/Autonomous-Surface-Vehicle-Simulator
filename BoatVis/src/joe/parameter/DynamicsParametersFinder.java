package joe.parameter;

import com.bulletphysics.linearmath.Transform;
import composites.entities.Entity;
import joe.parameter.csv.BoatCsvData;
import joe.parameter.physics.IsloatedPhysics;
import physics.util.PhysTransform;
import renderables.r3D.rotation.Quat4fHelper;
import renderables.r3D.water.RenderedWater;

import javax.vecmath.*;
import java.io.File;
import java.io.IOException;

/**
 * Created by CaptainPete on 8/5/2018.
 */
public class DynamicsParametersFinder {

    public static final float PHYSICS_TIMESTEP = 1.0f / 50.0f;

    public static final float SETTLE_TIME = 10.0f;

    private RenderedWater water;

    //private BoatCsvData boatCsvData;

    public static void main(String... args) {
        new DynamicsParametersFinder();
    }

    private DynamicsParametersFinder(){
        String path = "res/data_example.csv";


//        String pathLinear1 = "res/run3_from6s.csv";
//        String pathLinear2 = "res/run4_from11s.csv";
//        String pathAngular1 = "res/turn.csv";

        String pathpercent_20 = "res/speed_20_percent.csv";
        String pathpercent_40 = "res/speed_40_percent.csv";
        String pathpercent_60 = "res/speed_60_percent.csv";
        String pathpercent_80 = "res/speed_80_percent.csv";
        String pathpercent100 = "res/speed_100_percent.csv";

        File csvFile = new File(path);

//        BoatCsvData boatCsvDataLinear1 = new BoatCsvData(new File(pathLinear1));
//        BoatCsvData boatCsvDataLinear2 = new BoatCsvData(new File(pathLinear2));
//        BoatCsvData boatCsvDataAngular1 = new BoatCsvData(new File(pathAngular1));
        BoatCsvData boatCsvDataAngular_20 = new BoatCsvData(new File(pathpercent_20));
        BoatCsvData boatCsvDataAngular_40 = new BoatCsvData(new File(pathpercent_40));
        BoatCsvData boatCsvDataAngular_60 = new BoatCsvData(new File(pathpercent_60));
        BoatCsvData boatCsvDataAngular_80 = new BoatCsvData(new File(pathpercent_80));
        BoatCsvData boatCsvDataAngular100 = new BoatCsvData(new File(pathpercent100));

        this.water = new RenderedWater(null, null, null, null, null, -1, -1);

        float min_forwardThrustForceMultiplier = 80.0f;
        float min_backwardThrustForceMultiplier = 67.5f;
        float min_linearDamping = 0.2f;
        float min_angularDamping = 0.55f;

        float max_forwardThrustForceMultiplier = min_forwardThrustForceMultiplier;
        float max_backwardThrustForceMultiplier = min_backwardThrustForceMultiplier;
        float max_linearDamping = min_linearDamping;
        float max_angularDamping = min_angularDamping;

//        float max_forwardThrustForceMultiplier = 90.0f;
//        float max_backwardThrustForceMultiplier = 75.0f;
//        float max_linearDamping = 0.3f;
//        float max_angularDamping = 0.65f;

        float step_forwardThrustForceMultiplier = 2.5f;
        float step_backwardThrustForceMultiplier = 2.5f;
        float step_linearDamping = 0.05f;
        float step_angularDamping = 0.05f;

        float best_forwardThrustForceMultiplier = 100f;
        float best_backwardThrustForceMultiplier = 80f;
        float best_linearDamping = 0.3f;
        float best_angularDamping = 0.5f;

        double bestQualityMeasurement = Double.POSITIVE_INFINITY;

        double best_sumSquaredErrorLinear1 = 0f;
        double best_sumSquaredErrorLinear2 = 0f;
        double best_sumSquaredErrorAngular1 = 0f;
        //performTest(boatCsvDataAngular);


        //This a bit on the lame side, but it might work for now.
        for(float curr_forwardThrustForceMultiplier = min_forwardThrustForceMultiplier; curr_forwardThrustForceMultiplier <= max_forwardThrustForceMultiplier; curr_forwardThrustForceMultiplier += step_forwardThrustForceMultiplier){

            float numIterations = (max_forwardThrustForceMultiplier - min_forwardThrustForceMultiplier) / step_forwardThrustForceMultiplier;
            float elapsedIterations = (curr_forwardThrustForceMultiplier - min_forwardThrustForceMultiplier) / step_forwardThrustForceMultiplier;
            float percentComplete = elapsedIterations / numIterations;
            float percentComplete100 = percentComplete * 100.0f;
            System.out.println();
            System.out.println(percentComplete100 + "% complete.");

            for(float curr_backwardThrustForceMultiplier = min_backwardThrustForceMultiplier; curr_backwardThrustForceMultiplier <= max_backwardThrustForceMultiplier; curr_backwardThrustForceMultiplier += step_backwardThrustForceMultiplier){
                System.out.print("|");
                for(float curr_linearDamping = min_linearDamping; curr_linearDamping <= max_linearDamping; curr_linearDamping += step_linearDamping){
                    System.out.print(".");
                    for(float curr_angularDamping = min_angularDamping; curr_angularDamping <= max_angularDamping; curr_angularDamping += step_angularDamping){

//                        performTest(boatCsvDataLinear1, curr_forwardThrustForceMultiplier, curr_backwardThrustForceMultiplier, curr_linearDamping, curr_angularDamping);
//                        performTest(boatCsvDataLinear2, curr_forwardThrustForceMultiplier, curr_backwardThrustForceMultiplier, curr_linearDamping, curr_angularDamping);
//                        performTest(boatCsvDataAngular1, curr_forwardThrustForceMultiplier, curr_backwardThrustForceMultiplier, curr_linearDamping, curr_angularDamping);

                        performTest(boatCsvDataAngular_20, curr_forwardThrustForceMultiplier, curr_backwardThrustForceMultiplier, curr_linearDamping, curr_angularDamping);
                        performTest(boatCsvDataAngular_40, curr_forwardThrustForceMultiplier, curr_backwardThrustForceMultiplier, curr_linearDamping, curr_angularDamping);
                        performTest(boatCsvDataAngular_60, curr_forwardThrustForceMultiplier, curr_backwardThrustForceMultiplier, curr_linearDamping, curr_angularDamping);
                        performTest(boatCsvDataAngular_80, curr_forwardThrustForceMultiplier, curr_backwardThrustForceMultiplier, curr_linearDamping, curr_angularDamping);
                        performTest(boatCsvDataAngular100, curr_forwardThrustForceMultiplier, curr_backwardThrustForceMultiplier, curr_linearDamping, curr_angularDamping);

//                        float r2linear1 = boatCsvDataLinear1.getRSquared(boatCsvDataLinear1.getSpeeds(), boatCsvDataLinear1.getSimSpeeds());
//                        float r2linear2 = boatCsvDataLinear2.getRSquared(boatCsvDataLinear2.getSpeeds(), boatCsvDataLinear2.getSimSpeeds());
//                        float r2angular1 = boatCsvDataAngular1.getRSquared(boatCsvDataAngular1.getHeadingRates(), boatCsvDataAngular1.getHeadingRates());

//                        double sumSquaredErrorLinear1 = boatCsvDataLinear1.getSsRes(boatCsvDataLinear1.getSpeeds(), boatCsvDataLinear1.getSimSpeeds());
//                        double sumSquaredErrorLinear2 = boatCsvDataLinear2.getSsRes(boatCsvDataLinear2.getSpeeds(), boatCsvDataLinear2.getSimSpeeds());
//                        double sumSquaredErrorAngular1 = boatCsvDataLinear2.getSsRes(boatCsvDataAngular1.getHeadingRates(), boatCsvDataAngular1.getSimHeadingRates());

//                        double qualityMeasurement = sumSquaredErrorLinear1 * sumSquaredErrorLinear2 * sumSquaredErrorAngular1;
                        double qualityMeasurement = 1111111111111.0;

                        if(qualityMeasurement < bestQualityMeasurement){
                            bestQualityMeasurement = qualityMeasurement;
//                            best_sumSquaredErrorLinear1  = sumSquaredErrorLinear1;
//                            best_sumSquaredErrorLinear2  = sumSquaredErrorLinear2;
//                            best_sumSquaredErrorAngular1 = sumSquaredErrorAngular1;

                            best_forwardThrustForceMultiplier = curr_forwardThrustForceMultiplier;
                            best_backwardThrustForceMultiplier = curr_backwardThrustForceMultiplier;
                            best_linearDamping = curr_linearDamping;
                            best_angularDamping = curr_angularDamping;

                        }
                    }
                }
            }

            System.out.println("bestQualityMeasurement = " + bestQualityMeasurement);
            System.out.println("best_sumSquaredErrorLinear1  = " + best_sumSquaredErrorLinear1 );
            System.out.println("best_sumSquaredErrorLinear2  = " + best_sumSquaredErrorLinear2 );
            System.out.println("best_sumSquaredErrorAngular1 = " + best_sumSquaredErrorAngular1);
            System.out.println("best_forwardThrustForceMultiplier = " + best_forwardThrustForceMultiplier);
            System.out.println("best_backwardThrustForceMultiplier = " + best_backwardThrustForceMultiplier);
            System.out.println("best_linearDamping = " + best_linearDamping);
            System.out.println("best_angularDamping = " + best_angularDamping);
        }

        System.out.println();
        System.out.println("----------------------");
        System.out.println();

        System.out.println("bestQualityMeasurement = " + bestQualityMeasurement);
        System.out.println("best_sumSquaredErrorLinear1  = " + best_sumSquaredErrorLinear1 );
        System.out.println("best_sumSquaredErrorLinear2  = " + best_sumSquaredErrorLinear2 );
        System.out.println("best_sumSquaredErrorAngular1 = " + best_sumSquaredErrorAngular1);
        System.out.println("best_forwardThrustForceMultiplier = " + best_forwardThrustForceMultiplier);
        System.out.println("best_backwardThrustForceMultiplier = " + best_backwardThrustForceMultiplier);
        System.out.println("best_linearDamping = " + best_linearDamping);
        System.out.println("best_angularDamping = " + best_angularDamping);


        try {
            boatCsvDataAngular_20.writeToFile(boatCsvDataAngular_20.getFilePathWithNameAppend("_sim"));
            boatCsvDataAngular_40.writeToFile(boatCsvDataAngular_40.getFilePathWithNameAppend("_sim"));
            boatCsvDataAngular_60.writeToFile(boatCsvDataAngular_60.getFilePathWithNameAppend("_sim"));
            boatCsvDataAngular_80.writeToFile(boatCsvDataAngular_80.getFilePathWithNameAppend("_sim"));
            boatCsvDataAngular100.writeToFile(boatCsvDataAngular100.getFilePathWithNameAppend("_sim"));
            //TODO - Verify that this isn't going to BERK anything.
//            boatCsvDataLinear1.writeToFile(boatCsvDataLinear1.getFilePathWithNameAppend("_sim"));
//            boatCsvDataLinear2.writeToFile(boatCsvDataLinear2.getFilePathWithNameAppend("_sim"));
//            boatCsvDataAngular1.writeToFile(boatCsvDataAngular1.getFilePathWithNameAppend("_sim"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("100.00% Complete...");
    }


    private void performTest(BoatCsvData boatCsvData, float forwardThrustForceMultiplier, float backwardThrustForceMultiplier, float linearDamping, float angularDamping){

        IsloatedPhysics isloatedPhysics = new IsloatedPhysics(water, PHYSICS_TIMESTEP);
        isloatedPhysics.initPhysics();

        //Zero motor forces.
        isloatedPhysics.getWamV().applyConstantMotorForceOnLeftPontoon(0f);
        isloatedPhysics.getWamV().applyConstantMotorForceOnRightPontoon(0f);

        //Settle wamv.
        float timer = 0f;

        while (timer < SETTLE_TIME) {
            timer += PHYSICS_TIMESTEP;
            isloatedPhysics.updatePhysicsSingleStep(PHYSICS_TIMESTEP);

        }

        //We have now settled.



        //Determine initial settings:
        Transform initialWamVTransform = isloatedPhysics.getWamV().getWorldTransform();
        Vector3f initialPosition = PhysTransform.toGlPosition(initialWamVTransform.origin);

        Quat4f initialRotation = PhysTransform.toGlRotation(initialWamVTransform.basis);
        Vector3f initialXyzRotation = new Vector3f();
        Quat4fHelper.toXYZRotation(initialRotation, initialXyzRotation);
        float initialHeadingAngle = (float) Math.toDegrees(initialXyzRotation.x);


        //Create variables.
        Vector3f previousPosition = new Vector3f(initialPosition);
        Vector3f previousXyzRotation = new Vector3f(initialXyzRotation);

        timer = 0f;

        int csvIndex = 0;

        //Set changable variables.
        isloatedPhysics.getWamV().updateParameters(forwardThrustForceMultiplier, backwardThrustForceMultiplier, linearDamping, angularDamping);

        float timeBetweenDataPoint = 0f;

        boatCsvData.getSimTimes()[0] = 0f;
        boatCsvData.getSimSpeeds()[0] = 0f;
        boatCsvData.getSimHeadings()[0] = initialHeadingAngle;
        boatCsvData.getSimHeadingRates()[0] = 0f;
        boatCsvData.getSimLeftMotorPercentages()[0] = 0f;
        boatCsvData.getSimRightMotorPercentages()[0] = 0f;

        while (csvIndex < (boatCsvData.dataHeight() - 1)) {
            float currentLeftPercentage = boatCsvData.getLeftMotorPercentages()[csvIndex];
            float currentRightPercentage = boatCsvData.getRightMotorPercentages()[csvIndex];

            isloatedPhysics.getWamV().applyConstantMotorForceOnLeftPontoon(currentLeftPercentage / 100.0f);
            isloatedPhysics.getWamV().applyConstantMotorForceOnRightPontoon(currentRightPercentage / 100.0f);

            timer += PHYSICS_TIMESTEP;
            timeBetweenDataPoint += PHYSICS_TIMESTEP;
            isloatedPhysics.updatePhysicsSingleStep(PHYSICS_TIMESTEP);

            //Get current values.
            Transform wamVTransform = isloatedPhysics.getWamV().getWorldTransform();
            Vector3f currentPosition = PhysTransform.toGlPosition(wamVTransform.origin);

            Quat4f currentRotation = PhysTransform.toGlRotation(wamVTransform.basis);
            Vector3f currentXyzRotation = new Vector3f();
            Quat4fHelper.toXYZRotation(currentRotation, currentXyzRotation);
            float currentHeadingAngle = (float) Math.toDegrees(currentXyzRotation.x);

            float nextTimeStep = boatCsvData.getTimes()[csvIndex + 1];

            //Check if we've gone past the next data point:
            if (timer >= nextTimeStep) {
                //Move to the next time step.
                Vector3f travelledDistance = new Vector3f(currentPosition);
                travelledDistance.sub(previousPosition);
                previousPosition = new Vector3f(currentPosition);
                Vector2f xzMovement = new Vector2f(travelledDistance.x, travelledDistance.z);
                float distanceTravelled = xzMovement.length();
                float averageVelocity = distanceTravelled / timeBetweenDataPoint;


                float previousAngleDeg = (float) Math.toDegrees(previousXyzRotation.x);
                float angleDifference = currentHeadingAngle - previousAngleDeg;
                while (angleDifference < -180.0f){
                    angleDifference += 360.0f;
                }
                while (angleDifference > 180.0f){
                    angleDifference -= 360.0f;
                }
                float headingRate = angleDifference / timeBetweenDataPoint;
                previousXyzRotation = new Vector3f(currentXyzRotation);

                //Store sim data for this run.
                boatCsvData.getSimTimes()[csvIndex + 1] = timer;
                boatCsvData.getSimSpeeds()[csvIndex + 1] = averageVelocity;
                boatCsvData.getSimHeadings()[csvIndex + 1] = currentHeadingAngle;
                boatCsvData.getSimHeadingRates()[csvIndex + 1] = headingRate;
//                System.out.println("realHeadingRate = " + boatCsvData.getHeadingRates()[csvIndex + 1]);
//                System.out.println("simHeadingRate = " + boatCsvData.getSimHeadingRates()[csvIndex + 1]);
//                System.out.println("---");
                boatCsvData.getSimLeftMotorPercentages()[csvIndex + 1] = currentLeftPercentage;
                boatCsvData.getSimRightMotorPercentages()[csvIndex + 1] = currentRightPercentage;

                csvIndex++;
                timeBetweenDataPoint = 0f;
            }


        }

    }

    public static void setEntityTransform(Entity entity, Transform transform) {
        javax.vecmath.Vector3f glBoxPosition = PhysTransform.toGlPosition(transform.origin);
        setEntityPosition(entity, glBoxPosition);
        Quat4f glBoxRotation = PhysTransform.toGlRotation(transform.basis);
        setEntityRotation(entity, glBoxRotation);
    }

    public static void setEntityPosition(Entity entity, javax.vecmath.Vector3f position) {
        entity.setPosition(position.x, position.y, position.z);
    }

    public static void setEntitySize(Entity entity, javax.vecmath.Vector3f size) {
        entity.setSize(size.x, size.y, size.z);
    }

    public static void setEntityRotation(Entity entity, Tuple4f tuple4f) {
        entity.getQuatRotation().set(tuple4f);
        entity.getQuatRotation().inverse();
    }


}
