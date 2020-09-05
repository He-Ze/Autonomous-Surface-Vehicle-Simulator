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

	vec4 modelOrientation = in_Position * modelSize;
	vec4 worldPosition = modelOrientation + modelPos;

	vec4 toVertexPosition = (worldPosition - viewPos);
	//fragmentDistance = length(toVertexPosition);
	toVertexVector = toVertexPosition.xyz;
	
	gl_Position =  projectionRotationMatrix * toVertexPosition;
	//gl_Position.z = fragmentDistance / 100.0;
}