<script lang="ts">
 import { Form, Field } from "svelte-forms-lib";
 import * as yup from "yup";

 const formProps = {
   initialValues: {
     text: "",
     email: "",
     password: "",
     number: 0,
     date: ""
   },

   validationSchema: yup.object().shape({
     text: yup.string(),
     email: yup.string().email(),
     password: yup.string(),
     number: yup.number(),
     date: yup.date()
   }),

   onSubmit: (values: Record<string, unknown>) => {
     alert(JSON.stringify(values, null, 2));
   }
 };
</script>

<Form {...formProps}>
  <label for="text">input[type=text]</label>
  <Field name="text" type="text" />

  <label for="password">input[type=password]</label>
  <Field name="password" type="password" />

  <label for="email">input[type=email]</label>
  <Field name="email" type="email" />

  <label for="number">input[type=number]</label>
  <Field name="number" type="number" />

  <label for="date">input[type=date]</label>
  <Field name="date" type="date" />

  <button type="submit">submit</button>
</Form>
