#version 130

uniform mat4 projectionRotationMatrix;
uniform vec4 viewPos;
uniform vec3 lightPosition;
uniform vec4 modelPos;
uniform vec4 modelSize;
uniform vec4 modelRot;
uniform vec2 textureOffset;
uniform vec4 clipPlane;

in vec4 in_Position;
in vec2 in_TextureCoord;
in vec3 normal;

out vec2 pass_TextureCoord;
out vec3 surfaceNormal;
out vec3 toLightVector;
out float lightDistance;

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
	
	gl_Position =  projectionRotationMatrix * (worldPosition - viewPos);
	
	pass_TextureCoord = in_TextureCoord + textureOffset;
	
	surfaceNormal = (rotMatrix * vec4(normal, 0.0)).xyz;
	toLightVector =  lightPosition - worldPosition.xyz;
	
	//lightDistance = length(toLightVector);
	lightDistance = 1;
}