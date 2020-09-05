#version 130
//version 150 core

uniform sampler2D texture_diffuse;

//in float fragmentDistance;
in vec3 toVertexVector;

out vec4 out_Color;

void main(void) {
    //float nearPlane = 0.1; //TODO - MAKE THESE UNIFORM VARIABLES.
    float farPlane = 100.0; //TODO - MAKE THESE UNIFORM VARIABLES.
    //float depthToFragment = gl_FragCoord.z;
    //float lidarDistance = 2.0 * nearPlane * farPlane / (farPlane + nearPlane - ((2.0 * depthToFragment - 1.0) * (farPlane - nearPlane)));
    //TODO - This is going to have to be transformed depending the x, y fragment coordinates.
    //lidarDistance = fragmentDistance;
    //lidarDistance -= 1.5;
    //lidarDistance /= 0.5;
    //lidarDistance /= 10.0;
    //lidarDistance = 1.0 - depthToFragment;

    float lidarDistance = length(toVertexVector);
    //lidarDistance /= 5.0;

    if(lidarDistance >= farPlane){
        out_Color = vec4(1.0, 1.0, 1.0, 1.0);
    } else {
        float distanceFrom0To1 = lidarDistance / farPlane;
        int intDistanceToMax = int(distanceFrom0To1 * 256 * 256 * 256);

        const int redMask = 255 * 256 * 256;
        const int greenMask = 255 * 256;
        const int blueMask = 255;
        const vec4 bitMsk = vec4(1.0/256.0, 1.0/256.0, 1.0/256.0, 1.0);

        int redVal = (intDistanceToMax & redMask) >> 16;
        int greenVal = (intDistanceToMax & greenMask) >> 8;
        int blueVal = (intDistanceToMax & blueMask);

        out_Color = vec4(redVal, greenVal, blueVal, 1.0) * bitMsk;
        //out_Color = vec4(128, greenVal, blueVal, 1.0) * bitMsk;
    }


}