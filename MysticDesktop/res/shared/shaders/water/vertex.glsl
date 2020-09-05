#version 130

uniform mat4 projectionRotationMatrix;
uniform vec4 viewPos;
uniform vec3 lightPosition;
uniform vec4 modelPos;
uniform vec4 modelSize;

uniform vec2 tiling;

uniform float rotationWater;

uniform float waterRippleOffset;

uniform float waterWaveOffset;
uniform float waterWaveDirection;
uniform float waterWaveHeight;
uniform float distanceBetweenWaves;

uniform float waterWaveTime;


uniform float seaStateAValues[5];
uniform float seaStateWValues[5];
uniform float seaStateThetaValues[5];
uniform float seaStateKValues[5];

in vec4 in_Position;
in vec2 in_TextureCoord;
in vec3 normal;

out vec2 pass_TextureCoord;
out vec3 surfaceNormal;
out vec3 toLightVector;
out float lightDistance;

out vec3 toCameraVector;

out vec4 clipSpace;

void main(void) {

	vec4 modelOrientation = in_Position * modelSize;
	vec4 absoluteWorldPosition = modelOrientation + modelPos;

	vec4 worldPosition = absoluteWorldPosition - viewPos;

	//The water quad is always at the (x,z) of the camera position.


	//return waterHeight + (float) (2.3 * Math.sin((xPos + (waterDistortionOffset * 50)) / 20));
	//absoluteWorldPosition.y += 0.2 * sin((absoluteWorldPosition.x + (waterRippleOffset*50)) / 4);
	float rotatedPosition = (absoluteWorldPosition.x * cos(waterWaveDirection)) - (absoluteWorldPosition.z * sin(waterWaveDirection));
	//worldPosition.y += waterWaveHeight * sin((rotatedPosition/distanceBetweenWaves) + waterWaveOffset);

    //worldPosition.y += 1.0 * sin(waterWaveTime * seaStateAValues[1]);

    for(int i = 0; i < 5; i++){
        worldPosition.y += seaStateAValues[i] * cos((seaStateWValues[i] * waterWaveTime)
                + (seaStateKValues[i] * cos(seaStateThetaValues[i])  * absoluteWorldPosition.x)
                + (seaStateKValues[i] * sin(seaStateThetaValues[i])  * absoluteWorldPosition.z));
    }


    //worldPosition.y += 1.0 * sin(waterWaveTime * 3.14);
    //worldPosition.y += waterWaveTime;

	//absoluteWorldPosition.y += 0.3 * sin((absoluteWorldPosition.x + (waterDistortionOffset*50)) / 2) * sin(absoluteWorldPosition.z / 3);
	//absoluteWorldPosition.y += absoluteWorldPosition.x + absoluteWorldPosition.z;



	clipSpace = projectionRotationMatrix * worldPosition;
	gl_Position = clipSpace;

	mat2 quadRotMatrix = mat2(
        cos(rotationWater), -sin(rotationWater),
        sin(rotationWater), cos(rotationWater)
    );

    vec2 texCoordOffset = (viewPos.xz / modelSize.xz) * 0.5;
    vec2 usedTexCoord = in_TextureCoord + texCoordOffset;
	
	pass_TextureCoord = quadRotMatrix * ((usedTexCoord) * tiling);
	
	surfaceNormal = normal;
	toLightVector = (lightPosition - viewPos.xyz) - worldPosition.xyz;
	
	lightDistance = length(toLightVector);

	toCameraVector = -(worldPosition.xyz);
}