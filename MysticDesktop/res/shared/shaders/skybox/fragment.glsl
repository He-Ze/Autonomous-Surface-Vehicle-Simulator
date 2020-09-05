#version 130

uniform samplerCube cubeMap;

uniform vec3 textureBlendColour;
uniform float textureBlendAmount;

in vec3 textureCoords;
out vec4 out_Color;



void main(void) {
    vec4 textureColour = texture(cubeMap, textureCoords);
	out_Color = mix(textureColour, vec4(textureBlendColour.xyz, 1), textureBlendAmount);
}