#version 150 core

out vec4 out_Color;
in vec2 out_TexCoord;

uniform sampler2D depthMap;

float calculateDepth(in float d, in float d1, in float d2){
  const float a = 2.0; // Difference that gives max occlusion
  float dav = (d1+d2)/2.0;
  float x = d - dav;
  float dao = max( 1.0/a/a*x*(2.0*a - x), 0.0);
  return dao;
}
float readDepth( in vec2 coord )
{
    // These numbers must match the main program's perspective projection
    float zNear = 0.1f;
    float zFar = 50.0f;
    float z_from_depth_texture = texture(depthMap, coord).x;
    float z_sb = 2.0 * z_from_depth_texture - 1.0; // scale and bias from texture to normalized coords
    float z_world = 2.0 * zNear * zFar / (zFar + zNear - z_sb * (zFar - zNear)); // Get back to real Z
return z_world;
}
void main(void){
   float samples = 100.0f;
   float weight = 0.3f;
   float dx = 1.0f/600.0f;
   float dy = 1.0f/600.0f;
   float d1, d2, d;
   float ao = 0.0f;
   for(int i = 0; i<samples; i++){
     d = readDepth(out_TexCoord);
     d1 = readDepth( vec2(out_TexCoord.x + dx, out_TexCoord.y + dx));
     d2 = readDepth(vec2(out_TexCoord.x - dx, out_TexCoord.y - dy));
     ao += weight * calculateDepth(d, d1, d2);
     dx += 1.0f/600.0f;
     dy += 1.0f/600.0f;
     weight = 0.5f*(1-i/samples);
   }
   out_Color = vec4(vec3(ao), 1.0f);
  // out_Color = vec4(vec3(readDepth(out_TexCoord)/50.0f),1.0f);
}
