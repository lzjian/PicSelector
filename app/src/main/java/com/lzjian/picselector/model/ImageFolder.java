package com.lzjian.picselector.model;

public class ImageFolder {

    //图片文件夹路径
    private String dir;
    //第一张图片路径
    private String firstImagePath;
    //文件夹名字
    private String name;
    //图片的数量
    private int count;

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public String getFirstImagePath() {
        return firstImagePath;
    }

    public void setFirstImagePath(String firstImagePath) {
        this.firstImagePath = firstImagePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "ImageFolder{" +
                "dir='" + dir + '\'' +
                ", firstImagePath='" + firstImagePath + '\'' +
                ", name='" + name + '\'' +
                ", count=" + count +
                '}';
    }
}
