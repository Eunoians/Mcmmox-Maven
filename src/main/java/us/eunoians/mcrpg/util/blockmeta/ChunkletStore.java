package us.eunoians.mcrpg.util.blockmeta;

import java.io.Serializable;

/**
 * This code is not mine. It is copyright from the original mcMMO allowed for use by their license. Modified 12/7/18
 * It was released under the GPLv3 license
 */
/**
 * A ChunkletStore should be responsible for a 16x16x64 area of data
 */
public interface ChunkletStore extends Serializable {
  /**
   * Checks the value at the given coordinates
   *
   * @param x x coordinate in current chunklet
   * @param y y coordinate in current chunklet
   * @param z z coordinate in current chunklet
   * @return true if the value is true at the given coordinates, false if otherwise
   */
  boolean isTrue(int x, int y, int z);

  /**
   * Set the value to true at the given coordinates
   *
   * @param x x coordinate in current chunklet
   * @param y y coordinate in current chunklet
   * @param z z coordinate in current chunklet
   */
  void setTrue(int x, int y, int z);

  /**
   * Set the value to false at the given coordinates
   *
   * @param x x coordinate in current chunklet
   * @param y y coordinate in current chunklet
   * @param z z coordinate in current chunklet
   */
  void setFalse(int x, int y, int z);

  /**
   * @return true if all values in the chunklet are false, false if otherwise
   */
  boolean isEmpty();

  /**
   * Set all values in this ChunkletStore to the values from another provided ChunkletStore
   *
   * @param otherStore Another ChunkletStore that this one should copy all data from
   */
  void copyFrom(ChunkletStore otherStore);
}
