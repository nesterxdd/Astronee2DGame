package com.mygdx.game.world;

import java.util.Random;

/**
 * Represents a noise generator for creating random but smooth transitions in a 2D array.
 * This class is used for generating Perlin noise, which is a type of gradient noise often used in procedural texture generation.
 *
 * @author Antom Makasevych
 */
public class Noisy {
    /**
     * The seed for the random number generator.
     */
    public int seed;

    /**
     * The number of octaves to use in the noise generation.
     */
    public int octaves;

    /**
     * The persistence value controls the amplitude of each successive octave.
     */
    public float persistence;

    /**
     * Constructor for creating a Noisy instance.
     *
     * @param seed The seed for the random number generator.
     * @param octaves The number of octaves to use in the noise generation.
     * @param persistence The persistence value controls the amplitude of each successive octave.
     *
     * @author Antom Makasevych
     */
    public Noisy(int seed, int octaves, float persistence) {
        this.seed = seed;
        this.octaves = octaves;
        this.persistence = persistence;
    }

    /**
     * Generates a 2D array of Perlin noise.
     *
     * @param width The width of the array to generate.
     * @param height The height of the array to generate.
     * @return A 2D array of Perlin noise.
     *
     * @author Antom Makasevych
     */
    public float[][] generatePerlinNoise(int width, int height) {
        final float[][] base = new float[width][height];
        final float[][] perlinNoise = new float[width][height];
        final float[][][] noiseLayers = new float[octaves][][];

        Random random = new Random(seed);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                base[x][y] = random.nextFloat();
            }
        }

        for (int octave = 0; octave < octaves; octave++) {
            noiseLayers[octave] = generatePerlinNoiseLayer(base, width, height, octave);
        }

        float amplitude = 1f;
        float totalAmplitude = 0f;

        for (int octave = octaves - 1; octave >= 0; octave--) {
            amplitude *= persistence;
            totalAmplitude += amplitude;

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    perlinNoise[x][y] += noiseLayers[octave][x][y] * amplitude;
                }
            }
        }

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                perlinNoise[x][y] /= totalAmplitude;
            }
        }

        return perlinNoise;
    }

    /**
     * Generates a single layer of Perlin noise.
     *
     * @param base The base random float array.
     * @param width The width of the noise array.
     * @param height The height of the noise array.
     * @param octave The current layer.
     * @return A 2D array containing calculated "Perlin-Noise-Layer" values.
     *
     * @author Antom Makasevych
     */
    private static float[][] generatePerlinNoiseLayer(float[][] base, int width, int height, int octave) {
        float[][] perlinNoiseLayer = new float[width][height];

        //calculate period (wavelength) for different shapes
        int period = 1 << octave; //2^k
        float frequency = 1f / period; // 1/2^k

        for (int x = 0; x < width; x++) {
            //calculates the horizontal sampling indices
            int x0 = (x / period) * period;
            int x1 = (x0 + period) % width;
            float horizintalBlend = (x - x0) * frequency;

            for (int y = 0; y < height; y++) {
                //calculates the vertical sampling indices
                int y0 = (y / period) * period;
                int y1 = (y0 + period) % height;
                float verticalBlend = (y - y0) * frequency;

                //blend top corners
                float top = interpolate(base[x0][y0], base[x1][y0], horizintalBlend);

                //blend bottom corners
                float bottom = interpolate(base[x0][y1], base[x1][y1], horizintalBlend);

                //blend top and bottom interpolation to get the final blend value for this cell
                perlinNoiseLayer[x][y] = interpolate(top, bottom, verticalBlend);
            }
        }

        return perlinNoiseLayer;
    }

    /**
     * Interpolates between two points.
     *
     * @param a The value of point a.
     * @param b The value of point b.
     * @param alpha Determines which value has more impact (closer to 0 -> a, closer to 1 -> b).
     * @return The interpolated value.
     *
     * @author Antom Makasevych
     */
    private static float interpolate(float a, float b, float alpha) {
        return a * (1 - alpha) + alpha * b;
    }


}

