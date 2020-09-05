#version 130
//version 150 core

uniform sampler2D texture_diffuse;
uniform vec4 mixColour;
uniform float mixAmount;

uniform vec4 testColour;

in vec2 pass_TextureCoord;

in vec4 lighting_Colour;

out vec4 out_Color;


//https://www.youtube.com/watch?v=LKnqECcg6Gw
vec4 niceMix(in vec4 col1, in vec4 col2, in float mixAmount){
	float alphaVal = mix(col1.w, col2.w, mixAmount);
	vec4 col1Squared = pow(col1, vec4(2, 2, 2, 2));
	vec4 col2Squared = pow(col2, vec4(2, 2, 2, 2));
	vec4 finalCol = sqrt(mix(col1Squared, col2Squared, mixAmount));
	finalCol.w = alphaVal;
	return finalCol;
}

void main(void) {
	
	vec4 textureColour = texture2D(texture_diffuse, pass_TextureCoord);
	vec4 temp = mixColour;
	temp.w = textureColour.w * mixColour.w;
	//out_Color = niceMix(textureColour, temp, mixAmount);
	
	//out_Color = testColour;
	//out_Color = lighting_Colour * textureColour;
	out_Color = textureColour * lighting_Colour;
	
	//out_Color = mix(textureColour, temp, mixAmount);
}