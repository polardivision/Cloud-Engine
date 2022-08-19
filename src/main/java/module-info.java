module net.frooastside.engine {

  exports love.polardivision.engine.camera;

  exports love.polardivision.engine.glwrapper;
  exports love.polardivision.engine.glwrapper.framebuffer;
  exports love.polardivision.engine.glwrapper.framebuffer.attachments;
  exports love.polardivision.engine.glwrapper.query;
  exports love.polardivision.engine.glwrapper.texture;
  exports love.polardivision.engine.glwrapper.vertexarray;
  exports love.polardivision.engine.glwrapper.vertexarray.vertexbuffer;

  exports love.polardivision.engine.language;

  exports love.polardivision.engine.postprocessing;

  exports love.polardivision.engine.shader;
  exports love.polardivision.engine.shader.uniforms;

  exports love.polardivision.engine.userinterface;
  exports love.polardivision.engine.userinterface.elements;
  exports love.polardivision.engine.userinterface.elements.basic;
  exports love.polardivision.engine.userinterface.elements.render;
  exports love.polardivision.engine.userinterface.events;
  exports love.polardivision.engine.userinterface.renderer;

  exports love.polardivision.engine.utils;

  exports love.polardivision.engine.window;
  exports love.polardivision.engine.window.callbacks;
  exports love.polardivision.engine.window.cursor;
  exports love.polardivision.engine.window.hints;

  exports love.polardivision.engine.ygwrapper;

  requires java.desktop;

  requires transitive org.lwjgl;

  requires transitive org.lwjgl.assimp;
  requires transitive org.lwjgl.glfw;
  requires transitive org.lwjgl.nfd;
  requires transitive org.lwjgl.nuklear;
  requires transitive org.lwjgl.openal;
  requires transitive org.lwjgl.opengl;
  requires transitive org.lwjgl.opus;
  requires transitive org.lwjgl.stb;
  requires transitive org.lwjgl.vulkan;
  requires transitive org.lwjgl.yoga;

  requires transitive org.joml;

}