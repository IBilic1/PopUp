/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.model;

import javafx.scene.image.ImageView;

/**
 *
 * @author HT-ICT
 */
public class Sprite {

    private ImageView imageView;
    public final static int DELTA_FOR_SPRITE = 3;
    public static int LIVES = 0;

    public Sprite(ImageView imageView) {
        this.imageView = imageView;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }
    
    

}
