package BananaFructa.TTIEMultiblocks.Utils;

public class RotaryRequirement {

    public float minTorque;
    public float minSpeed;
    public float maxSpeed;

    public RotaryRequirement(float minTorque,float minSpeed,float maxSpeed) {
        this.minTorque = minTorque;
        this.minSpeed = minSpeed;
        this.maxSpeed = maxSpeed;
    }

    public boolean fulfilsRequirement(float torque, float speed) {
        return torque >= minTorque && speed >= minSpeed && speed <= maxSpeed;
    }

}
