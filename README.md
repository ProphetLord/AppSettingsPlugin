# IntelliJ Platform Plugin Template

<!-- Plugin description -->
**AppSettings Plugin Template** is a tool plug-in based on the Clion code tool to quickly generate Qt configuration files.

By configuring the xml file and creating a field group according to the specified format requirements, right-click in the xml file and **Generate Settings Configuration File** to generate the corresponding configuration file in the current xml directory.([GitHub address][github:template])

[github:template]: https://docs.github.com/en/repositories/creating-and-managing-repositories/creating-a-repository-from-a-template
<!-- Plugin description end -->

The current project is based on ** [Intellij Platform Plugin Template][Intellij_Template] **


[Intellij_Template]: https://github.com/JetBrains/intellij-platform-plugin-template


### xml Configuration File Description

The xml configuration file [`template.xml`][file:template.xml], Initial setup of xml file:

| settings members         | Description                                                                    |
|--------------------------|--------------------------------------------------------------------------------|
| `ini`                    | The ini file is named, and eventually all configurations will be in this file. |
| `namespace`              | Declare the namespace for configuration items.                                 |
| `cmakeName`              | Naming the generated cmake file configuration item.                            |
| `version`                | Version number, temporarily useless..                                          |

The following describes the configuration fields and how to use them:

| Field name               | Description                                                                                                                                                                                                   |
|--------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `enums`                  | Create an enumeration class.                                                                                                                                                                                  |
| `type`                   | Create a custom type structure, which has a special item on the dialog box: **Type class setting Addr**, which will generate an enumeration of Addr. The field type in this item contains the set enums item. |
| `class`                  | Create a static data Type, which can contain items: type, enums.                                                                                                                                              |

[file:template.xml]: ./template/0.0.1/template.xml