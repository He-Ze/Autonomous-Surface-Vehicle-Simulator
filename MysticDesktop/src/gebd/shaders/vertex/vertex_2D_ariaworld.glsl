#version 130

uniform vec2 quadPos;
	
uniform vec2 quadSize;

uniform vec2 screenRes;

uniform vec2 textureCoord;

uniform vec2 textureSize;

uniform int lightingColor[4];

in vec4 in_Position;

out vec2 pass_TextureCoord;

out vec4 lighting_Colour;

void main(void) {
	
	vec2 relativePosition = vec2(in_Position) * (quadSize/2);
	
	vec2 screenPosition = (relativePosition + quadPos);
	
	gl_Position =  vec4((screenPosition.x - (screenRes.x/2)) / (screenRes.x/2), (screenPosition.y - (screenRes.y/2)) / (screenRes.y/2), -1, 1);
	
	vec2 rawUV = vec2((in_Position.x + 1)/2, 1 - ((in_Position.y + 1)/2));
	
	pass_TextureCoord = (rawUV * textureSize) + textureCoord;
	
	int index = int((2 * rawUV.x) + rawUV.y);
	int colVal = lightingColor[index];
	
	const int redMask = 255 * 256 * 256;
	const int greenMask = 255 * 256;
	const int blueMask = 255;
	const vec4 bitMsk = vec4(1.0/256.0, 1.0/256.0, 1.0/256.0, 1.0/256.0);
	
	int redVal = (colVal & redMask) >> 16;
	int greenVal = (colVal & greenMask) >> 8;
	int blueVal = (colVal & blueMask);
	
	vec4 colTFS = vec4(redVal, greenVal, blueVal, 256);
	lighting_Colour = colTFS * bitMsk;
	
	//const vec4 bitSh = vec4(256.0*256.0*256.0, 256.0*256.0, 256.0, 0);
  	//vec4 res = fract(bitSh - colVal);
  	//vec4 bytes = ivec4(colVal);
  	//const vec4 bitSh = vec4(256.0*256.0*256.0, 256.0*256.0, 256.0, 0);
  	//lighting_Colour &= bytes;
  	//const vec4 bitMsk = vec4(1.0/256.0, 1.0/256.0, 1.0/256.0, 1.0/256.0);
  	//lighting_Colour = normalize(bytes * bitMsk);
  	//lighting_Colour.w = 1;
	//lighting_Colour = vec4(index/4f, 0, bytes.z, 1);
}