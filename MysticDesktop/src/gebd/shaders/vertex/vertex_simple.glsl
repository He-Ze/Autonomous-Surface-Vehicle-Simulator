#version 130

uniform mat4 projectionRotationMatrix;
uniform vec4 viewPos;
uniform vec3 lightPosition;
uniform vec4 modelPos;
uniform vec2 textureOffset;

in vec4 in_Position;
in vec2 in_TextureCoord;
in vec3 normal;

out vec2 pass_TextureCoord;
out vec3 surfaceNormal;
out vec3 toLightVector;
out float lightDistance;

void main(void) {
	vec4 worldPosition = in_Position + modelPos - viewPos;
	
	gl_Position =  projectionRotationMatrix * worldPosition;
	
	pass_TextureCoord = in_TextureCoord + textureOffset;
	
	//surfaceNormal = (modelRotYAxis * (modelRotXAxis * (modelRotZAxis * vec4(normal, 0.0)))).xyz;
	surfaceNormal = normal;
	toLightVector = (lightPosition - viewPos.xyz) - worldPosition.xyz;
	
	lightDistance = length(toLightVector);
}