#version 150 core


out vec4 out_Color;
in vec2 out_TexCoord;
in mat3 out_TBN;
in vec3 surf;

uniform sampler2D texUnit;
uniform sampler2D normalMap;

void main(void) {
    //Light vectors
    vec3 lightDirection = vec3( 2.0f, 2.0f, -2.0f);
    vec3 lightDir   = normalize(lightDirection - surf);

    //Texture sampling
    vec3 tex = texture(texUnit, out_TexCoord).rgb;

    //Calculate normal from normalmap with tangent and bitangent
    vec3 normal = texture(normalMap, out_TexCoord).rgb;
    normal = normalize(normal * 2 - 1);
    normal = normalize(out_TBN* normal);

    //Ambient light
    vec3 ambient = 0.05 * tex;

    //Diffuse light
    float diff = max(dot(lightDir, normal), 0.0f);
    vec3 diffuse = diff * tex;

    //Specular light
    float shininess = 16.0f;
	vec3 viewDir = normalize(-surf);
    vec3 halfwayDir = normalize(lightDir + viewDir);
    float spec = pow(max(dot(normal, halfwayDir), 0.0f), shininess);
    vec3 specular = vec3(1.0f) * spec;

    //Sum of calculated light
    out_Color = vec4(ambient + diffuse + specular, 1.0);
    }

