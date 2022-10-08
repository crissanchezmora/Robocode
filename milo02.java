/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package meurobot;
import robocode.*;
import robocode.AdvancedRobot;
import robocode.ScannedRobotEvent;
import java.awt.*;
import static robocode.util.Utils.normalRelativeAngleDegrees;

/**
 *
 * @author crist
 */
public class milo02 extends AdvancedRobot {
    
    String enemyName = "";
    double enemyDistance = 5000;
    boolean aimToEnemy = false;
    double enemyBearing = 0;
    
    private double potencia(){
        double distancia = enemyDistance;
        if(distancia < 50) return Rules.MAX_BULLET_POWER;
        double amplada = getBattleFieldWidth();
        double alçada = getBattleFieldHeight();
        double diagonal = Math.sqrt(amplada*amplada + alçada*alçada);   //apliquem teorema Pitàgores
        double potencia = ((Rules.MAX_BULLET_POWER+Rules.MAX_BULLET_POWER
                *20/100)/diagonal)*enemyDistance;
        return Rules.MAX_BULLET_POWER-potencia;
    }
    public void run(){
        setAdjustGunForRobotTurn(true);
        setAdjustRadarForGunTurn(true);
        
        //0 because we are in the UL corner 
        turnRight(normalRelativeAngleDegrees(0 - getHeading()));
        ahead(5000);
        turnLeft(90);
        ahead(50000);

        while (true){
            setTurnRadarRightRadians(Double.POSITIVE_INFINITY);
            execute();
            if (aimToEnemy){
                setTurnGunRight(getHeading()-getGunHeading()+enemyBearing);
                setFire(potencia());
            }
        }
    }
    
    
    
    //Returns the degrees the gun has to turn right to aim to 
    /*private double target(ScannedRobotEvent e){
        
    }*/
    
    public void onScannedRobot(ScannedRobotEvent e){
       /*if (isTeammate(e.getName())){
            return;
        }
        */
        //If there is a enemy closer than the one we are currently aiming
        aimToEnemy = true;
        if (!enemyName.equals(e.getName()) && enemyDistance > e.getDistance()){
            enemyName = e.getName(); //Update new enemy
            setDebugProperty("New enemy name",""+e.getName());
            enemyDistance = e.getDistance(); //Update enemy distance
            setDebugProperty("New enemy distance",""+e.getDistance());
            enemyBearing = e.getBearing(); 
            setDebugProperty("New enemy bearing",""+e.getBearing());
        }
        else if(enemyName.equals(e.getName())){
            enemyDistance = e.getDistance();
            setDebugProperty("Updated enemy distance",""+e.getDistance());
            enemyBearing = e.getBearing();
            setDebugProperty("Updated enemy bearing",""+e.getBearing());
            //update distance for future scans and bearing to adjust the aiming
        }
    }
}
