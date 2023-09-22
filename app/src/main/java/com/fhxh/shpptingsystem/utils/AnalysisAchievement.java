package com.fhxh.shpptingsystem.utils;


import com.fhxh.shpptingsystem.ui.bean.PaintXY;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by  on 2021/7/26 13:34.
 */
public class AnalysisAchievement {
    private static final double HoldingGunt_YOUXIU = 5.0;
    private static final double HoldingGunt_LIANGHAO = 10.0;
    private static final double HoldingGunt_HEGE = 15.0;
    private static final double HoldingGunt_ZERO = 30.0;
    private static final double HoldingGunt_ZHANBI = 0.25;

    private static final double AimFractions_YOUXIU = 3.0;
    private static final double AimFractions_LIANGHAO = 5.0;
    private static final double AimFractions_HEGE = 8.0;
    private static final double AimFractions_ZERO = 16.0;
    private static final double AimFractions_ZHANBI = 0.25;

    private static final double ShotFractions_YOUXIU = 7.5;
    private static final double ShotFractions_LIANGHAO = 12.5;
    private static final double ShotFractions_HEGE = 15.0;
    private static final double ShotFractions_ZERO = 60.0;
    private static final double ShotFractions_ZHANBI = 0.25;

    private static final double AchieveFractions_YOUXIU = 10.0;
    private static final double AchieveFractions_LIANGHAO = 8.0;
    private static final double AchieveFractions_HEGE = 6.0;
    private static final double AchieveFractions_ZERO = 0.0;
    private static final double AchieveFractions_ZHANBI = 0.25;

    public static final double TotalFractions_YOUXIU = 90.0;
    public static final double TotalFractions_LIANGHAO = 80.0;
    public static final double TotalFractions_HEGE = 60.0;

    public static int[] AnalysisAchieve(List<PaintXY> analysis, List<PaintXY> shots,
                                        int CentreX, int CentreY, double EveryPxEqMillimeter, double ringNumber) {

        int[] achieves = new int[5];
        double XShit = 0;
        double YShit = 0;
        double XYShit = 0;

        for (int i = 0; i < analysis.size(); i++) {
            XShit = XShit + analysis.get(i).getX();
            YShit = YShit + analysis.get(i).getY();
            double shift_XY = Math.sqrt(Math.pow(XShit, 2) + Math.pow(YShit, 2));
            XYShit = XYShit + shift_XY;
        }
        XShit = XShit / ((double) analysis.size());
        YShit = YShit / ((double) analysis.size());
        XYShit = XYShit / ((double) analysis.size());


        int achieves0 = 0;
        if (XShit <= HoldingGunt_YOUXIU || YShit <= HoldingGunt_YOUXIU) {
            if (HoldingGunt_YOUXIU - XShit >= HoldingGunt_YOUXIU - YShit) {
                achieves0 = (int) (90.0 + ((HoldingGunt_YOUXIU - XShit) / HoldingGunt_YOUXIU) * 10.0);
            } else {
                achieves0 = (int) (90.0 + ((HoldingGunt_YOUXIU - YShit) / HoldingGunt_YOUXIU) * 10.0);
            }
        } else if ((XShit > HoldingGunt_YOUXIU && XShit <= HoldingGunt_LIANGHAO) ||
                (YShit > HoldingGunt_YOUXIU && YShit <= HoldingGunt_LIANGHAO)) {
            if ((HoldingGunt_LIANGHAO - XShit) >= (HoldingGunt_LIANGHAO - YShit)) {
                achieves0 = (int) (80.0 + ((HoldingGunt_LIANGHAO - XShit) / (HoldingGunt_LIANGHAO - HoldingGunt_YOUXIU)) * 10.0);
            } else {
                achieves0 = (int) (80.0 + ((HoldingGunt_LIANGHAO - YShit) / (HoldingGunt_LIANGHAO - HoldingGunt_YOUXIU)) * 10.0);
            }
        } else if ((XShit > HoldingGunt_LIANGHAO && XShit <= HoldingGunt_HEGE) ||
                (YShit > HoldingGunt_LIANGHAO && YShit <= HoldingGunt_HEGE)) {
            if ((HoldingGunt_HEGE - XShit) >= (HoldingGunt_HEGE - YShit)) {
                achieves0 = (int) (60.0 + ((HoldingGunt_HEGE - XShit) / (HoldingGunt_HEGE - HoldingGunt_LIANGHAO)) * 20.0);
            } else {
                achieves0 = (int) (60.0 + ((HoldingGunt_HEGE - YShit) / (HoldingGunt_HEGE - HoldingGunt_LIANGHAO)) * 20.0);
            }
        } else if ((XShit > HoldingGunt_HEGE && XShit <= HoldingGunt_ZERO) ||
                (YShit > HoldingGunt_HEGE && YShit <= HoldingGunt_ZERO)) {
            if ((HoldingGunt_ZERO - XShit) >= (HoldingGunt_ZERO - YShit)) {
                achieves0 = (int) (0.0 + ((HoldingGunt_ZERO - XShit) / (HoldingGunt_ZERO - HoldingGunt_HEGE)) * 60.0);
            } else {
                achieves0 = (int) (0.0 + ((HoldingGunt_ZERO - YShit) / (HoldingGunt_ZERO - HoldingGunt_HEGE)) * 60.0);
            }
        }
        if (XShit != -1 && YShit != -1) {
            achieves[0] = achieves0;
        }

        int achieves1 = 0;
        if (XYShit <= AimFractions_YOUXIU) {
            achieves1 = (int) (90.0 + ((AimFractions_YOUXIU - XYShit) / AimFractions_YOUXIU) * 10.0);
        } else if (XYShit > AimFractions_YOUXIU && XYShit <= AimFractions_LIANGHAO) {
            achieves1 = (int) (80.0 + (AimFractions_LIANGHAO - XYShit) / (AimFractions_LIANGHAO - AimFractions_YOUXIU) * 10.0);
        } else if (XYShit > AimFractions_LIANGHAO && XYShit <= AimFractions_HEGE) {
            achieves1 = (int) (60.0 + (AimFractions_HEGE - XYShit) / (AimFractions_HEGE - AimFractions_LIANGHAO) * 20.0);
        } else if (XYShit > AimFractions_HEGE && XYShit <= AimFractions_ZERO) {
            achieves1 = (int) (0.0 + (AimFractions_ZERO - XYShit) / (AimFractions_ZERO - AimFractions_HEGE) * 60.0);
        }
        if (XShit != -1 && YShit != -1) {
            achieves[1] = achieves1;
        }

        int achieves2 = 100;
        double XXX = 0;
        double YYY = 0;
        ArrayList<Integer> integers = new ArrayList<>();
        if (shots.size() > 1) {
            for (int i = 0; i < shots.size(); i++) {
                if (shots.get(i).getX() != -1) {
                    XXX = XXX + shots.get(i).getX();
                    YYY = YYY + shots.get(i).getY();
                    integers.add(i);
                }
            }
            if (integers.size() > 0) {
                double Mdis;
                if (integers.size() > 1) {
                    double cenx = XXX / integers.size();
                    double ceny = YYY / integers.size();
                    int MaxIndex = 0;
                    for (int i = 1; i < integers.size() - 1; i++) {
                        if (Math.pow(Math.abs(shots.get(integers.get(i)).getX() - cenx), 2) + Math.pow(Math.abs(shots.get(integers.get(i)).getY() - ceny), 2) >
                                Math.pow(Math.abs(shots.get(integers.get(MaxIndex)).getX() - cenx), 2) + Math.pow(Math.abs(shots.get(integers.get(MaxIndex)).getY() - ceny), 2)) {
                            MaxIndex = i;
                        }
                    }
                    double XXXX = XXX - shots.get(integers.get(MaxIndex)).getX();
                    double YYYY = YYY - shots.get(integers.get(MaxIndex)).getY();
                    integers.remove(MaxIndex);
                    XXXX = XXXX / integers.size();
                    YYYY = YYYY / integers.size();
                    Mdis = Math.sqrt(Math.pow(Math.abs(XXXX - CentreX), 2) + Math.pow(Math.abs(YYYY - CentreY), 2)) * EveryPxEqMillimeter;
                    Mdis = Mdis + (shots.size() - integers.size() + 1) * ShotFractions_YOUXIU;
                } else {
                    Mdis = Math.sqrt(Math.pow(Math.abs(shots.get(integers.get(0)).getX() - CentreX), 2) +
                            Math.pow(Math.abs(shots.get(integers.get(0)).getY() - CentreY), 2)) * EveryPxEqMillimeter;
                    Mdis = Mdis + (shots.size() - integers.size()) * ShotFractions_YOUXIU;
                }
                if (Mdis <= ShotFractions_YOUXIU) {
                    achieves2 = (int) (90.0 + ((ShotFractions_YOUXIU - Mdis) / ShotFractions_YOUXIU) * 10.0);
                } else if (Mdis > ShotFractions_YOUXIU && Mdis <= ShotFractions_LIANGHAO) {
                    achieves2 = (int) (80.0 + (ShotFractions_LIANGHAO - Mdis) / (ShotFractions_LIANGHAO - ShotFractions_YOUXIU) * 10.0);
                } else if (Mdis > ShotFractions_LIANGHAO && Mdis <= ShotFractions_HEGE) {
                    achieves2 = (int) (60.0 + (ShotFractions_HEGE - Mdis) / (ShotFractions_HEGE - ShotFractions_LIANGHAO) * 20.0);
                } else if (Mdis > ShotFractions_HEGE && Mdis <= ShotFractions_ZERO) {
                    achieves2 = (int) (0.0 + (ShotFractions_ZERO - Mdis) / (ShotFractions_ZERO - ShotFractions_HEGE) * 60.0);
                } else {
                    achieves2 = 60;
                }
            } else {
                achieves2 = 0;
            }
        } else if (shots.size() == 1) {
            if (shots.get(0).getX() == -1) {
                achieves2 = 0;
            }
        }
        achieves[2] = achieves2;

        int achieves3 = 0;
        double source = ringNumber;

        if (source >= AchieveFractions_YOUXIU) {
            achieves3 = (int) (90.0 + ((source - AchieveFractions_YOUXIU) / (10.9 - AchieveFractions_YOUXIU)) * 10.0);
        } else if (source < AchieveFractions_YOUXIU && source >= AchieveFractions_LIANGHAO) {
            achieves3 = (int) (80.0 + ((source - AchieveFractions_LIANGHAO) / (AchieveFractions_YOUXIU - AchieveFractions_LIANGHAO) * 10.0));
        } else if (source < AchieveFractions_LIANGHAO && source >= AchieveFractions_HEGE) {
            achieves3 = (int) (60.0 + ((source - AchieveFractions_HEGE) / (AchieveFractions_LIANGHAO - AchieveFractions_HEGE) * 20.0));
        } else if (source < AchieveFractions_HEGE && source >= AchieveFractions_ZERO) {
            achieves3 = (int) (0.0 + ((source - AchieveFractions_ZERO) / (AchieveFractions_HEGE - AchieveFractions_ZERO) * 60.0));
        }
        achieves[3] = achieves3;

        achieves[4] = (int) (achieves0 * HoldingGunt_ZHANBI + achieves1 * AimFractions_ZHANBI +
                achieves2 * ShotFractions_ZHANBI + achieves3 * AchieveFractions_ZHANBI);

        return achieves;
    }

}
