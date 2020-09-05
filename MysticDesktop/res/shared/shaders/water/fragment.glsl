#version 130
//version 150 core

uniform sampler2D reflectionTexture;
uniform sampler2D refractionTexture;
uniform sampler2D dudvMapTexture;
uniform sampler2D normalMapTexture;
uniform sampler2D depthMap;

uniform float luminosity;
uniform float ambientLightIntensity;
uniform vec3 waterColour;

uniform float waterRippleOffset;

uniform float waterMurkiness;
uniform float waterReflectivity;

in vec2 pass_TextureCoord;
in vec3 surfaceNormal;
in vec3 toLightVector;
in float lightDistance;

in vec4 clipSpace;
in vec3 toCameraVector;

uniform vec3 lightColour;

out vec4 out_Color;

uniform float waveStrength;
const float shineDamper = 30.0;
const float reflectivity = 0.6;

void main(void) {

	vec3 unitNormal = normalize(surfaceNormal);
	vec3 unitLightVector = normalize(toLightVector);
	
	float nDotl = dot(unitNormal, unitLightVector) * max((1 - pow((lightDistance * luminosity), 2)), 0);
	float brightness = max(nDotl, ambientLightIntensity);
	vec3 diffuse = brightness * lightColour;
	
	//Test for transparent objects, don't render if alpha < 0.5
	//This is inefficient, should fix later!

	//vec4 textureColour = texture2D(texture_diffuse, pass_TextureCoord);

    vec2 ndc = ((clipSpace.xy / clipSpace.w) / 2.0) + 0.5;
    vec2 reflectTexCoords = vec2(ndc.x, -ndc.y);
    vec2 refractTexCoords = vec2(ndc.x, ndc.y);


    float nearPlane = 0.5; //TODO - MAKE THESE UNIFORM VARIABLES.
    float farPlane = 1000.0; //TODO - MAKE THESE UNIFORM VARIABLES.
    float depthToSeaFloor = texture2D(depthMap, refractTexCoords).r;
    float floorDistance = 2.0 * nearPlane * farPlane / (farPlane + nearPlane - ((2.0 * depthToSeaFloor - 1.0) * (farPlane - nearPlane)));
    float depthToWater = gl_FragCoord.z;
    float waterDistance = 2.0 * nearPlane * farPlane / (farPlane + nearPlane - ((2.0 * depthToWater - 1.0) * (farPlane - nearPlane)));
    float waterDepth = floorDistance - waterDistance;

    //TODO - Consider a wider FOV for the reflection and refraction textures...
    vec2 distortedTexCoords = texture2D(dudvMapTexture, vec2(pass_TextureCoord.x + waterRippleOffset, pass_TextureCoord.y)).rg * 0.1;
    distortedTexCoords = pass_TextureCoord + vec2(distortedTexCoords.x, distortedTexCoords.y + waterRippleOffset);
    distortedTexCoords.x *= 0.2;
    vec2 totalDistortion = ((texture2D(dudvMapTexture, distortedTexCoords).rg * 2.0) - 1.0) * waveStrength * clamp(waterDepth/3.0, 0.0, 1.0);
    //vec2 totalDistortion = ((texture2D(dudvMapTexture, distortedTexCoords).rg * 2.0) - 1.0) * waveStrength;


    reflectTexCoords += totalDistortion;
    reflectTexCoords.x = clamp(reflectTexCoords.x, 0.005, 0.995);
    reflectTexCoords.y = clamp(reflectTexCoords.y, -0.995, -0.005);

    refractTexCoords += totalDistortion;
    refractTexCoords.x = clamp(refractTexCoords.x, 0.005, 0.995);
    refractTexCoords.y = clamp(refractTexCoords.y, 0.005, 0.995);

    //reflectTexCoords = vec2(0.5, 0.5);

	vec4 reflectColour = texture2D(reflectionTexture, reflectTexCoords);
	vec4 refractColour = texture2D(refractionTexture, refractTexCoords);


    //Normal map.
    vec4 normalMapColour = texture2D(normalMapTexture, distortedTexCoords);
    vec3 normal = vec3((normalMapColour.r * 2.0) - 1.0, normalMapColour.b * 2.0, (normalMapColour.g * 2.0) - 1.0);
    normal = normalize(normal);

    //Fresnel Effect
    vec3 viewVector = normalize(toCameraVector);
    float refractiveFactor = dot(viewVector, normal);
    refractiveFactor = pow(refractiveFactor, waterReflectivity * 0.5);
    refractiveFactor = clamp(refractiveFactor, 0.0, 1.0);



    vec3 reflectedLight = reflect(normalize(-toLightVector), normal);
    float specular = max(dot(reflectedLight, viewVector), 0.0);
    specular = pow(specular, shineDamper);
    vec3 specularHighlights = lightColour * specular * reflectivity;

	//TODO this is the correct output line
	//out_Color = vec4(diffuse, 1.0) * textureColour;

    //TODO - Fresnel Effect.
    //TODO - Consider using the niceMix();
	out_Color = mix(reflectColour, refractColour, refractiveFactor);
	out_Color = mix(out_Color, vec4(waterColour.xyz, 1), waterMurkiness) + vec4(specularHighlights, 0.0);

    //Essentiall equivalent to the call below it, but handles the case where water is rendered first.
	out_Color = mix(out_Color, refractColour, 1.0 - clamp(waterDepth/0.5, 0.0, 1.0));
    //out_Color.a = clamp(waterDepth/0.5, 0.0, 1.0);


    //out_Color = vec4(waterDepth/50.0, waterDepth/50.0, waterDepth/50.0, 1); //This can be used to vizualize the depth of the water at any given point.

    //out_Color = vec4(floorDistance/50.0, floorDistance/50.0, floorDistance/50.0, 1); //This can be used to vizualize lidar readings under the water.

	//out_Color = vec4(reflectColour.xyz, 1);

	//out_Color = vec4(waterColour, 1.0);

	//out_Color = vec4(reflectColour.xyz, 1);
	//out_Color = vec4(refractColour.xyz, 1);

	//out_Color = texture2D(texture_diffuse, pass_TextureCoord);
	//out_Color = texture2D(dudvMapTexture, pass_TextureCoord);
}