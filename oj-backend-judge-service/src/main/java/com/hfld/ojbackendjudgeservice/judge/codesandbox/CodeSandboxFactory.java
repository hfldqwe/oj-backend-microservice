package com.hfld.ojbackendjudgeservice.judge.codesandbox;


import com.hfld.ojbackendjudgeservice.judge.codesandbox.impl.ExampleCodeSandbox;
import com.hfld.ojbackendjudgeservice.judge.codesandbox.impl.RemoteCodeSandbox;
import com.hfld.ojbackendjudgeservice.judge.codesandbox.impl.ThirdCodeSandbox;

/**
 * 代码沙箱工厂(根据字符串参数创建指定代码沙箱创建)
 */
public class CodeSandboxFactory {

    /**
     * 创建代码沙箱实例
     *
     * @param type: 沙箱类型
     * @return
     */
    public static CodeSandbox newInstance(String type) {
        switch (type) {
            case "example":
                return new ExampleCodeSandbox();
            case "remote":
                return new RemoteCodeSandbox();
            case "thirdParty":
                return new ThirdCodeSandbox();
            default:
                return new ExampleCodeSandbox();
        }

    }
}
