echo "Lines of code in Java files:"
find . -name '*.java' -exec wc -l {} +

echo "Number of source files:"
find . -name '*.java' | wc -l

echo "Number of dependencies:"
mvn dependency:tree | grep -c '.*[\\\+]-.*'