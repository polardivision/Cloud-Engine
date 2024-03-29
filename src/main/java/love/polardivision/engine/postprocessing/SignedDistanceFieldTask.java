/*
 * Copyright © 2022-2023 @Frooastside
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package love.polardivision.engine.postprocessing;

import java.nio.ByteBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import love.polardivision.engine.utils.BufferUtils;
import org.joml.Vector2f;

public class SignedDistanceFieldTask {

  private static final ExecutorService executorService =
      Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

  private final ByteBuffer sourceBuffer;
  private final int imageSize;
  private final int downscale;
  private final float spread;
  private final int length;

  private final AtomicInteger progress = new AtomicInteger();
  private final ByteBuffer distanceFieldBuffer;

  public SignedDistanceFieldTask(
      ByteBuffer sourceBuffer, int imageSize, int downscale, float spread) {
    this.sourceBuffer = sourceBuffer;
    this.imageSize = imageSize;
    this.downscale = downscale;
    this.spread = spread;
    this.length = (int) Math.pow((float) imageSize / (float) downscale, 2);
    this.distanceFieldBuffer = ByteBuffer.allocateDirect(length);
  }

  public void generate() {
    byte[] pixelArray = BufferUtils.copyToArray(sourceBuffer);
    boolean[][] bitmap = new boolean[imageSize][imageSize];
    for (int y = 0; y < imageSize; y++) {
      for (int x = 0; x < imageSize; x++) {
        byte rgb = pixelArray[y * imageSize + x];
        bitmap[x][y] = rgb < 0;
      }
    }
    int downscaledImageSize = imageSize / downscale;
    for (int y = 0; y < downscaledImageSize; y++) {
      for (int x = 0; x < downscaledImageSize; x++) {
        final int centerX = x * downscale + downscale / 2;
        final int centerY = y * downscale + downscale / 2;
        final boolean base = bitmap[centerX][centerY];
        final int delta = (int) Math.ceil(spread);
        final int startX = Math.max(0, centerX - delta);
        final int startY = Math.max(0, centerY - delta);
        final int endX = Math.min(imageSize - 1, centerX + delta);
        final int endY = Math.min(imageSize - 1, centerY + delta);
        int finalY = y;
        int finalX = x;
        executorService.submit(
            () -> {
              int closestSquareDistance = delta * delta;
              for (int j = startY; j < endY; j++) {
                for (int i = startX; i < endX; i++) {
                  if (base != bitmap[i][j]) {
                    int squareDistance = (int) Vector2f.distanceSquared(centerX, centerY, i, j);
                    if (squareDistance < closestSquareDistance) {
                      closestSquareDistance = squareDistance;
                    }
                  }
                }
              }
              float closestDistance = (float) Math.sqrt(closestSquareDistance);
              float distance = (base ? 1 : -1) * Math.min(closestDistance, spread);
              float alpha = 0.5f + 0.5f * distance / spread;
              int alphaByte = (int) (Math.min(1.0f, Math.max(0.0f, alpha)) * 255.0f);
              synchronized (distanceFieldBuffer) {
                distanceFieldBuffer.put(finalY * downscaledImageSize + finalX, (byte) alphaByte);
              }
              synchronized (progress) {
                progress.incrementAndGet();
              }
            });
      }
    }
  }

  public void waitForCompletion() {
    try {
      executorService.shutdown();
      if (executorService.awaitTermination(1, TimeUnit.HOURS)) {
        throw new TimeoutException();
      }
    } catch (InterruptedException | TimeoutException ignored) {
    }
  }

  public int length() {
    return length;
  }

  public synchronized float progress() {
    return ((float) ((1.0 / length) * progress.get()));
  }

  public synchronized boolean finished() {
    return progress.get() == length;
  }

  public ByteBuffer distanceFieldBuffer() {
    return finished() ? distanceFieldBuffer : null;
  }
}
