#ifdef GL_ES
precision mediump float;
#endif

varying vec2 v_texCoords;

uniform sampler2D u_scene;       // Scene render
uniform sampler2D u_light;       // Light FBO from RayHandler
uniform vec3  u_ambientColor;    // Ambient color multiplier
uniform float u_gain;            // Light strength scaling

vec3 toLinear(vec3 c) { return pow(c, vec3(2.2)); }
vec3 toGamma (vec3 c) { return pow(c, vec3(1.0 / 2.2)); }

void main() {
    vec3 scene    = texture2D(u_scene, v_texCoords).rgb;
    vec3 lightTex = texture2D(u_light, v_texCoords).rgb;

    // Convert to linear space for correct light math
    vec3 sceneLin = toLinear(scene);
    vec3 lightLin = toLinear(lightTex) * u_gain;

    // 1) Ambient * scene → keeps day/night tone
    vec3 ambientLit = sceneLin * u_ambientColor;

    // 2) Add light contribution on top
    vec3 finalLit = ambientLit + lightLin;

    // 3) Tone map (optional clamp)
    finalLit = clamp(finalLit, 0.0, 1.0);

    // Back to gamma space for display
    vec3 lit = toGamma(finalLit);

    gl_FragColor = vec4(lit, 1.0);
}
