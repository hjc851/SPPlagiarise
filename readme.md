# SPPlagiarise: A tool for generating semantics-preserving variants of Java source code.

SPPlagiarise is a proof-of-concept tool for generating semantics-preserving clones of Java source code. It is designed to apply semantics-preserving obfuscations of complexity which can be considered to be performed by a novice programmer attempting to obfuscate plagiarism.
A selection of 20 obfuscations may be randomly applied to a project.

The design of the application heavily relies upon dependency injection and contexts, and hence integrates CDI (Weld) to manage dependencies.

There is absolutely no support or documentation provided with this application. It served as a tool for a research project. This tool is in no way production quality, and has not been verified to always produce syntactically correct variants.

### Running SPPlagiarise

Running SPPlagiarise on any application first requires a configuration file. An example is provided in the 'example' directory. Essentially, any source files need to be placed in the 'src' directory, and any .jar dependencies in the 'lib' directory.

To run SPPlagiarise, simply execute the 'runner' sub-project's run task through gradle with the path to a valid configuration file.

Variants of the input projects will be placed in numbered sub-directories of the 'out' directory. i.e. out/1, out/2, out/3, ..., out/n.

### Extending SPPlagiarise

SPPlagiarise is designed with a pipe-and-filter architecture. Each 'filter' literally applies a single obfuscation to the source code. New obfuscations can be applied by creating a new DSTObfuscationFilter subclass, implementing the obfuscation logic, and configuring the DSTObfuscationFilterFactory class to inject a new instance of the filter.