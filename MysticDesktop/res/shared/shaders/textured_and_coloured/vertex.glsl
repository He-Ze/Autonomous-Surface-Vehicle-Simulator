#version 130

uniform vec2 quadPos;
	
uniform vec2 quadSize;
	
uniform float quadRotation;

uniform vec2 screenRes;

uniform vec2 textureCoord;

uniform vec2 textureSize;

in vec4 in_Position;

out vec2 pass_TextureCoord;

void main(void) {
	
	vec2 relativePosition = vec2(in_Position) * (quadSize/2);
	
	mat2 quadRotMat = mat2(
		cos(quadRotation), -sin(quadRotation),
		sin(quadRotation), cos(quadRotation)
	);
	
	vec2 quadOrientation = (quadRotMat * (relativePosition));
	
	vec2 screenPosition = (quadOrientation + quadPos);
	
	gl_Position =  vec4((screenPosition.x - (screenRes.x/2)) / (screenRes.x/2), (screenPosition.y - (screenRes.y/2)) / (screenRes.y/2), -1, 1);
	
	vec2 rawUV = vec2((in_Position.x + 1)/2, 1 - ((in_Position.y + 1)/2));
	
	pass_TextureCoord = (rawUV * textureSize) + textureCoord;
}