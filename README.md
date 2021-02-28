# diffutil-coverbean
对bean使用注解标记来自动生成recycleview的差分callback类

## 依赖
目前未传到远程仓库，只能clone下来

```
    kapt project(path: ':diff_gen_compile')
    api project(path: ':diff_gen_annotation')
```
## 使用
假定列表Item数据实体为TestBean

### 创建一个AsyncListDiffer的ItemCallback
```
@DiffAsynch
public class TestBean {
    @DiffId
    String id;
    @DiffItem
    String values;
    int age;
    @DiffItem
    String sex;
   //以下为get，set方法且不能少
```
- 使用 **@DiffAsynch** 标记该类将创建一个AsyncListDiffer的ItemCallback
- 使用 **@DiffId** 标记将视作item比对的id，只能有一个
- 使用 **@DiffItem** 标记对比项， 可以有多个

生成如下

```
public final class TestBeanItemCallbackGenerate extends ItemCallback<TestBean> {
  @Override
  public boolean areItemsTheSame(TestBean oldItem, TestBean newItem) {
    return TextUtils.equals(oldItem.getId()+"", newItem.getId()+"");
  }

  @Override
  public boolean areContentsTheSame(TestBean oldItem, TestBean newItem) {
    return TextUtils.equals(oldItem.getValues()+"", newItem.getValues()+"")&&TextUtils.equals(oldItem.getSex()+"", newItem.getSex()+"");
  }
}
```
### 创建一个DiffUtil的Callback
```
@DiffSynch
public class TestBean {
    @DiffId
    String id;
    @DiffItem
    String values;
    int age;
    @DiffItem
    String sex;
   //以下为get，set方法且不能少
```
- 使用 **@DiffSynch** 标记该类将创建一个DiffUtil的Callback
- 使用 **@DiffId** 标记将视作item比对的id，只能有一个
- 使用 **@DiffItem** 标记对比项， 可以有多个

生成如下

```
public final class TestBeanCallbackGenerate extends Callback {
  List<TestBean> oldItems;

  List<TestBean> newItems;

  public TestBeanCallbackGenerate(List<TestBean> oldItems, List<TestBean> newItems) {
    this.oldItems = oldItems;
    this.newItems = newItems;
  }

  @Override
  public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
    return TextUtils.equals(oldItems.get(oldItemPosition).getId()+"", newItems.get(newItemPosition).getId()+"");
  }

  @Override
  public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
    TestBean oldItem = oldItems.get(oldItemPosition);
    TestBean newItem = newItems.get(newItemPosition);
    return TextUtils.equals(oldItem.getValues()+"", newItem.getValues()+"")&&TextUtils.equals(oldItem.getSex()+"", newItem.getSex()+"");
  }

  @Override
  public int getOldListSize() {
    return oldItems != null ? oldItems.size() : 0;
  }

  @Override
  public int getNewListSize() {
    return newItems != null ? newItems.size() : 0;
  }
}
```
生成的类名 = ‘bean类名’ + 类型 +  'Generate'
