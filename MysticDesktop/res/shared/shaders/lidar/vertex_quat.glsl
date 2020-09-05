#version 130

uniform mat4 projectionRotationMatrix;
uniform vec4 viewPos;
uniform vec4 modelPos;
uniform vec4 modelSize;
uniform vec4 modelRot;
uniform vec4 clipPlane;

in vec4 in_Position;
in vec2 in_TextureCoord;
in vec3 normal;

//out float fragmentDistance;
out vec3 toVertexVector;

void main(void) {


    float qx = modelRot.x;
    float qx2 = qx * qx;
    float qy = modelRot.y;
    float qy2 = qy * qy;
    float qz = modelRot.z;
    float qz2 = qz * qz;
    float qw = modelRot.w;
    //float qw2 = qw * qw; //Not required.


    mat4 rotMatrix = mat4(
        1 - 2*qy2 - 2*qz2,	2*qx*qy - 2*qz*qw,	2*qx*qz + 2*qy*qw,  0,
        2*qx*qy + 2*qz*qw,	1 - 2*qx2 - 2*qz2,	2*qy*qz - 2*qx*qw,  0,
        2*qx*qz - 2*qy*qw,	2*qy*qz + 2*qx*qw,	1 - 2*qx2 - 2*qy2,  0,
        0,                  0,                  0,                  1
    );

	vec4 modelOrientation = rotMatrix * (in_Position * modelSize);

	vec4 worldPosition = modelOrientation + modelPos;

	gl_ClipDistance[0] = dot(worldPosition, clipPlane);

	vec4 toVertexPosition = (worldPosition - viewPos);
	//fragmentDistance = length(toVertexPosition);
	toVertexVector = toVertexPosition.xyz;
	
	gl_Position =  projectionRotationMatrix * toVertexPosition;
	//gl_Position.z = fragmentDistance / 100.0;
}