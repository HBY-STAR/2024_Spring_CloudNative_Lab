# ase_user_service

## 外部接口
- /user/login 用户登陆
- /user/register 用户注册
- /user/logout 用户注销

详细文档见apifox中user_service的接口文档

## 内部接口
- `org.fd.ase.gro15.ase_user_service.service.IUserService` 用户服务接口（登陆、注册、注销）
- `org.fd.ase.gro15.ase_user_service.service.IUserConferenceRoleService` 用户会议角色服务接口

详细内容可见上述两个类中的注释。其他服务可以使用dubbo的`@DubboReference`注解，以远程调用的方式调用这两个服务。