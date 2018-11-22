#version 150 core


out vec4 out_Color;
in vec2 out_TexCoord;
in mat3 out_TBN;
in vec3 surf;
in vec4 shadowCoord;

uniform sampler2D texUnit;
uniform sampler2D normalMap;
uniform sampler2D depthMap;

uniform mat4 cameraMatrix;
uniform vec3 lightPosition;
uniform mat4 modelView;
uniform int isLit;

float readDepth( in vec2 coord )
{
    float zNear = 0.1f;
    float zFar = 100.0f;
    float z_from_depth_texture = texture(depthMap, coord).x;
    float z_sb = 2.0 * z_from_depth_texture - 1.0; // scale and bias from texture to normalized coords
    float z_world = 2.0 * zNear * zFar / (zFar + zNear - z_sb * (zFar - zNear)); // Get back to real Z
    return z_world;
}

void main(void) {
    //Light vectors
    vec3 lightDir   = normalize(vec3(cameraMatrix*vec4(lightPosition,1.0)) - surf);

    //Texture sampling
    vec3 tex = texture(texUnit, out_TexCoord).rgb;

    //Calculate normal from normalmap with tangent and bitangent
    vec3 normal = texture(normalMap, out_TexCoord).rgb;
    normal = normalize(normal * 2 - 1);
    normal = normalize(out_TBN* normal);

    //Ambient light
    vec3 ambient = 0.3f * tex;

    //Diffuse light
    float diff = max(dot(lightDir, normal), 0.0f);
    vec3 diffuse = diff * tex;

    //Specular light
    float shininess = 16.0f;
	vec3 viewDir = normalize(-surf.xyz);
    vec3 halfwayDir = normalize(lightDir + viewDir);
    float spec = pow(max(dot(normal, halfwayDir), 0.0f), shininess);
    vec3 specular = vec3(0.3f) * spec;

    vec3 adjustedShadowCoords = (shadowCoord.xyz / shadowCoord.w) * 0.5 + 0.5;
    float shade = 1.0;
    if(texture(depthMap, adjustedShadowCoords.xy).r + 0.001f < adjustedShadowCoords.z){
        shade = 0.5f;
    }

    out_Color = vec4(shade*(ambient + diffuse + specular), 1.0);

    if(isLit == 1){
        out_Color = vec4(0,0,0,0);
    }
}

