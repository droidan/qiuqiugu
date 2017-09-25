package com.tiger.socol.gu.api.order;

public class ExpressCostEntity {

    private String payer;

    private CostsBean costs;

    public double price(double expressWeight) {
        if (payer.equals("seller")) {
            return 0;
        }

        if (expressWeight <= 0) {
            return 0;
        }

        if (expressWeight < costs.firstWeight) {
            return costs.firstCost;
        }

        double p = expressWeight - costs.firstWeight;
        double x = p / costs.incrWeight;
        int i = (int) x;
        if ((x - i) > 0) {
            i++;
        }
        double m = (double) i * costs.incrCost;
        return costs.firstCost + m;
    }

    public String getPayer() {
        return payer;
    }

    public void setPayer(String payer) {
        this.payer = payer;
    }

    public CostsBean getCosts() {
        return costs;
    }

    public void setCosts(CostsBean costs) {
        this.costs = costs;
    }

    public static class CostsBean {
        /**
         * 首重kg
         */
        private double firstWeight;

        /**
         * 首重费用
         */
        private double firstCost;

        /**
         * 续重kg
         */
        private double incrWeight;

        /**
         * 超出首重后单价
         */
        private double incrCost;

        public double getFirstWeight() {
            return firstWeight;
        }

        public void setFirstWeight(double firstWeight) {
            this.firstWeight = firstWeight;
        }

        public double getFirstCost() {
            return firstCost;
        }

        public void setFirstCost(double firstCost) {
            this.firstCost = firstCost;
        }

        public double getIncrWeight() {
            return incrWeight;
        }

        public void setIncrWeight(double incrWeight) {
            this.incrWeight = incrWeight;
        }

        public double getIncrCost() {
            return incrCost;
        }

        public void setIncrCost(double incrCost) {
            this.incrCost = incrCost;
        }
    }

}
