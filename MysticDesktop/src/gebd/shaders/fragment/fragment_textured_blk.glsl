#version 130
//version 150 core

uniform sampler2D texture_diffuse;
uniform float luminosity;
uniform float ambientLightIntensity;

in vec2 pass_TextureCoord;
in vec3 surfaceNormal;
in vec3 toLightVector;
in float lightDistance;
in float brightness;

uniform vec3 lightColour;

out vec4 out_Color;

void main(void) {

	vec3 unitNormal = normalize(surfaceNormal);
	vec3 unitLightVector = normalize(toLightVector);
	
	float nDotl = dot(unitNormal, unitLightVector) * max((1 - pow((lightDistance * luminosity), 2)), 0);
	float lightBrightness = max(nDotl, ambientLightIntensity);
	vec3 diffuse = lightBrightness * lightColour;
	
	//Test for transparent objects, don't render if alpha < 0.5
	//This is inefficient, should fix later!
	vec4 textureColour = texture2D(texture_diffuse, pass_TextureCoord);
	if(textureColour.a < 0.5){
		discard;
	}
	//brightness
	out_Color = vec4((brightness * textureColour.rgb), textureColour.a);
	//out_Color = vec4(brightness, 1.0, brightness, textureColour.a);
	//out_Color = vec4(diffuse, 1.0) * textureColour;
	//out_Color = texture2D(texture_diffuse, pass_TextureCoord);
}