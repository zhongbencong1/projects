package com.faker.project.designpattern;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 责任链模式
 */
@SuppressWarnings("all")
public class Chain {

    @AllArgsConstructor
    @Data
    public static class LeaveRequest {
        //请假人姓名
        private String name;
        //请假天数
        private Integer days;
    }

    public static abstract class Approver {
        String name;
        Approver next;

        public Approver(String name){
            this.name=name;
        }

        //设置下一个审批者
        public void setNext(Approver next){
            this.next=next;
        }

        //审批请求
        public abstract void approve(LeaveRequest request);
    }


    public static class Master extends Approver {
        public Master(String name) {super(name);}

        @Override
        public void approve(LeaveRequest request) {
            if (request.getDays() > 7) {
                System.out.println("校长" + name + "审批了" + request.getName() + "的请假申请，天数为" + request.getDays());
            } else {
                if (next != null) {
                    next.approve(request);
                }
            }
        }
    }

    public static class GradeLeader extends Approver {
        public GradeLeader(String name) {super(name);}

        @Override
        public void approve(LeaveRequest request) {
            if (request.getDays() <= 7) {
                System.out.println("年级组长" + name + "审批" + request.getName() + "的请假申请，天数为" + request.getDays());
            } else {
                if (next != null) {
                    System.out.println("年级组长审批不了，交由下一级");
                    next.approve(request);
                }
            }
        }
    }

    public static void main(String[] args) {
        Approver gradeLeader = new GradeLeader("李四");
        Approver schoolMaster = new Master("王五");

        //组织责任链
        gradeLeader.setNext(schoolMaster);

        //发起请求
        LeaveRequest request = new LeaveRequest("小明", 6);
        gradeLeader.approve(request);
    }
}
